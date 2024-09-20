package com.example.security.util;

import java.util.*;

import com.example.security.model.Permission;
import com.example.security.model.Role;

import javax.servlet.http.HttpServletRequest;

import com.example.security.model.User;

import com.example.security.service.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.support.BeanDefinitionDsl;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class TokenUtils {
    @Autowired
    IRoleService roleService;

    // Izdavac tokena
    @Value("spring-security-example")
    private String APP_NAME;

    // Tajna koju samo backend aplikacija treba da zna kako bi mogla da generise i proveri JWT https://jwt.io/
    @Value("somesecret")
    public String SECRET;

    // Period vazenja tokena - 30 minuta
    @Value("1800000")
    private int EXPIRES_IN;

    // Naziv headera kroz koji ce se prosledjivati JWT u komunikaciji server-klijent
    @Value("Authorization")
    private String AUTH_HEADER;

    // Moguce je generisati JWT za razlicite klijente (npr. web i mobilni klijenti nece imati isto trajanje JWT,
    // JWT za mobilne klijente ce trajati duze jer se mozda aplikacija redje koristi na taj nacin)
    // Radi jednostavnosti primera, necemo voditi racuna o uređaju sa kojeg zahtev stiže.
    //	private static final String AUDIENCE_UNKNOWN = "unknown";
    //	private static final String AUDIENCE_MOBILE = "mobile";
    //	private static final String AUDIENCE_TABLET = "tablet";

    private static final String AUDIENCE_WEB = "web";

    // Algoritam za potpisivanje JWT
    private SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.HS512;


    // ============= Funkcije za generisanje JWT tokena =============

    /**
     * Funkcija za generisanje JWT tokena.
     *
     * @param username Korisničko ime korisnika kojem se token izdaje
     * @return JWT token
     */
    public String generateToken(String username, List<String> role) {
        Date expirationDate = new Date(System.currentTimeMillis() + 15*60*1000);//15 min traje access

        List<String> permissions=new ArrayList<>();
        for(String r:role) {
            List<Role> roles = roleService.findByName(r);

            for(Role r1:roles){
                for(Permission p:r1.getPermissions()){
                    permissions.add(p.getName());
                }
            }

        }
        return Jwts.builder()
                .setIssuer(APP_NAME)
                .setSubject(username)
                .claim("role", role)
                .claim("permission",permissions)
                .setAudience(generateAudience())
                .setIssuedAt(new Date())
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS256, "SECRET").compact();
    }
    public String generateRefreshToken(String username) {
        long timestamp = System.currentTimeMillis();

        Map<String, Object> claims = new HashMap<>();
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(generateRefreshExpirationDate())
                .signWith(SignatureAlgorithm.HS256, "SECRET").compact();
    }

    public static String generatePasswordlesToken(String username) {
        long timestamp = System.currentTimeMillis();
        return Jwts.builder()
                .setSubject(username)
                .claim("timestamp", timestamp)
                .signWith(SignatureAlgorithm.HS256, "SECRET")
                .compact();
    }

    private Date generateRefreshExpirationDate() {
        return new Date(System.currentTimeMillis() + 604800 * 1000);
    }

    /**
     * Funkcija za utvrđivanje tipa uređaja za koji se JWT kreira.
     * @return Tip uređaja.
     */
    private String generateAudience() {

        //	Moze se iskoristiti org.springframework.mobile.device.Device objekat za odredjivanje tipa uredjaja sa kojeg je zahtev stigao.
        //	https://spring.io/projects/spring-mobile

        //	String audience = AUDIENCE_UNKNOWN;
        //		if (device.isNormal()) {
        //			audience = AUDIENCE_WEB;
        //		} else if (device.isTablet()) {
        //			audience = AUDIENCE_TABLET;
        //		} else if (device.isMobile()) {
        //			audience = AUDIENCE_MOBILE;
        //		}

        return AUDIENCE_WEB;
    }

    /**
     * Funkcija generiše datum do kog je JWT token validan.
     *
     * @return Datum do kojeg je JWT validan.
     */
    private Date generateExpirationDate() {
        return new Date(new Date().getTime() + EXPIRES_IN);
    }

    // =================================================================

    // ============= Funkcije za citanje informacija iz JWT tokena =============

    /**
     * Funkcija za preuzimanje JWT tokena iz zahteva.
     *
     * @param request HTTP zahtev koji klijent šalje.
     * @return JWT token ili null ukoliko se token ne nalazi u odgovarajućem zaglavlju HTTP zahteva.
     */
    public String getToken(HttpServletRequest request) {
        String authHeader = getAuthHeaderFromHeader(request);
        System.out.println("Auth"+authHeader);

        // JWT se prosledjuje kroz header 'Authorization' u formatu:
        // Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7); // preuzimamo samo token (vrednost tokena je nakon "Bearer " prefiksa)
        }

        return null;
    }

    /**
     * Funkcija za preuzimanje vlasnika tokena (korisničko ime).
     * @param token JWT token.
     * @return Korisničko ime iz tokena ili null ukoliko ne postoji.
     */
    public String getUsernameFromToken(String token) {
        String username;

        try {
            username = this.getAllClaimsFromToken(token);
            //System.out.println(claims);
            //username = claims.getSubject();
        } catch (ExpiredJwtException ex) {
            throw ex;
        } catch (Exception e) {
            username = null;
        }

        return username;
    }

    /**
     * Funkcija za preuzimanje datuma kreiranja tokena.
     * @param token JWT token.
     * @return Datum kada je token kreiran.
     */
    /*public Date getIssuedAtDateFromToken(String token) {
        Date issueAt;
        try {
            final Claims claims = this.getAllClaimsFromToken(token);
            issueAt = claims.getIssuedAt();
        } catch (ExpiredJwtException ex) {
            throw ex;
        } catch (Exception e) {
            issueAt = null;
        }
        return issueAt;
    }*/

    /**
     * Funkcija za preuzimanje informacije o uređaju iz tokena.
     *
     * @param token JWT token.
     * @return Tip uredjaja.
     */
    /*public String getAudienceFromToken(String token) {
        String audience;
        try {
            final Claims claims = this.getAllClaimsFromToken(token);
            audience = claims.getAudience();
        } catch (ExpiredJwtException ex) {
            throw ex;
        } catch (Exception e) {
            audience = null;
        }
        return audience;
    }*/

    /**
     * Funkcija za preuzimanje datuma do kada token važi.
     *
     * @param token JWT token.
     * @return Datum do kojeg token važi.
     */
    /*public Date getExpirationDateFromToken(String token) {
        Date expiration;
        try {
            final Claims claims = this.getAllClaimsFromToken(token);
            expiration = claims.getExpiration();
        } catch (ExpiredJwtException ex) {
            throw ex;
        } catch (Exception e) {
            expiration = null;
        }

        return expiration;
    }*/

    /**
     * Funkcija za čitanje svih podataka iz JWT tokena
     *
     * @param token JWT token.
     * @return Podaci iz tokena.
     */
    private String getAllClaimsFromToken(String token) {
        Claims claims;
        String usernameRefresh="";
        try {
            Jws<Claims> claimsRefresh = Jwts.parser()
                    .setSigningKey("SECRET")
                    .parseClaimsJws(token);
            usernameRefresh = claimsRefresh.getBody().getSubject();
        } catch (ExpiredJwtException ex) {
            throw ex;
        } catch (Exception e) {
            claims = null;
        }

        // Preuzimanje proizvoljnih podataka je moguce pozivom funkcije claims.get(key)

        return usernameRefresh;
    }

    // =================================================================

    // ============= Funkcije za validaciju JWT tokena =============

    /**
     * Funkcija za validaciju JWT tokena.
     *
     * @param token JWT token.
     * @param userDetails Informacije o korisniku koji je vlasnik JWT tokena.
     * @return Informacija da li je token validan ili ne.
     */
    public Boolean validateToken(String token, UserDetails userDetails) {
        User user = (User) userDetails;
        final String username = getUsernameFromToken(token);
        //final Date created = getIssuedAtDateFromToken(token);

        // Token je validan kada:
        return (username != null // korisnicko ime nije null
                && username.equals(userDetails.getUsername()) // korisnicko ime iz tokena se podudara sa korisnickom imenom koje pise u bazi
                ); // nakon kreiranja tokena korisnik nije menjao svoju lozinku
    }

    /**
     * Funkcija proverava da li je lozinka korisnika izmenjena nakon izdavanja tokena.
     *
     * @param created Datum kreiranja tokena.
     * @param lastPasswordReset Datum poslednje izmene lozinke.
     * @return Informacija da li je token kreiran pre poslednje izmene lozinke ili ne.
     */
    /*private Boolean isCreatedBeforeLastPasswordReset(Date created, Date lastPasswordReset) {
        return (lastPasswordReset != null && created.before(lastPasswordReset));
    }*/

    // =================================================================

    /**
     * Funkcija za preuzimanje perioda važenja tokena.
     *
     * @return Period važenja tokena.
     */
    public int getExpiredIn() {
        return EXPIRES_IN;
    }

    /**
     * Funkcija za preuzimanje sadržaja AUTH_HEADER-a iz zahteva.
     *
     * @param request HTTP zahtev.
     *
     * @return Sadrzaj iz AUTH_HEADER-a.
     */
    public String getAuthHeaderFromHeader(HttpServletRequest request) {
        return request.getHeader(AUTH_HEADER);
    }

}