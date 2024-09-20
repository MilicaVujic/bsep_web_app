import { Component } from '@angular/core';
import { AuthService } from 'src/app/login/auth.service';
import { AdvertisementRequest, RequestStatus } from 'src/app/models/advertisement-request.model';
import { User } from 'src/app/models/user.model';
import { ProfileService } from 'src/app/profile/profile.service';
import { AdvertisementService } from '../advertisement.service';
import { AppComponent } from 'src/app/app.component';
import { Router } from '@angular/router';
import { SanitizeService } from 'src/app/sanitize.service';

@Component({
  selector: 'app-create-advertisement-request',
  templateUrl: './create-advertisement-request.component.html',
  styleUrls: ['./create-advertisement-request.component.css']
})
export class CreateAdvertisementRequestComponent {
  advertisement: AdvertisementRequest = {
    id:0,
    description: '',
    clientId: 0,
    expirationTime:'',
    activityStart: '',
    activityEnd: '',
    requestStatus: RequestStatus.NEW,
  };
  user:any;
  client:User;
  constructor(private sanitizeService:SanitizeService,private authService:AuthService,private clientService:ProfileService,private service:AdvertisementService,private appComponent:AppComponent, private router:Router
  ) { }
  ngOnInit(): void {
    this.user=this.authService.getUser()
    console.log(this.user);
    this.clientService.getClient(this.user.sub).subscribe({
      next:(result)=>{
        this.client=result;
        console.log(this.client)
        this.advertisement.clientId=this.client.id;
      },
      error:(error) => {
        if (error.status === 401) {
          alert(
            "Unauthorized. Maybe you should refresh your access token. "
          );
        }
      }
    })

  }

  submitForm() {
    var hasPermission = false;
    var permissions = this.authService.getRolePermissions();
    permissions?.forEach(p => {
      if(p=="CREATE_ADV_REQUEST") {
        hasPermission = true;
        
      }

    })
    if(hasPermission){
      this.advertisement.description=this.sanitizeService.sanitizeInput(this.advertisement.description)
      this.advertisement.expirationTime=this.sanitizeService.sanitizeInput(this.advertisement.expirationTime)
      this.advertisement.activityStart=this.sanitizeService.sanitizeInput(this.advertisement.activityStart)
      this.advertisement.activityEnd=this.sanitizeService.sanitizeInput(this.advertisement.activityEnd)
      if(this.advertisement.description!='' && this.advertisement.activityEnd && this.advertisement.activityStart && this.advertisement.expirationTime){
        if(this.IsDateValid()){
          console.log(this.advertisement);
          this.service.createAdvertisementRequest(this.advertisement).subscribe({
            next: (result) => {
              if(result==null)
                alert("Invalid data.")
              else {
                console.log('Zahtjev uspešno kreiran!', result);
                alert('Created.')
                this.router.navigate(['home_page']);
              }
              
            },
            error:(error) => {
              if (error.status === 401) {
                alert(
                  "Unauthorized. Maybe you should refresh your access token. "
                );
              }
            }
          })
        }else{
          alert('Start activity must be before end activity. End activity must be before or equal expiration date.')
        }
        console.log(this.advertisement);
      }else{
        alert('Fill fields.');
      }
      }else{
        alert('No permission.');
      }
      
   
  }

  IsDateValid():boolean{
    const now = new Date();
    const start = new Date(this.advertisement.activityStart);
    const end = new Date(this.advertisement.activityEnd);
    const expiration = new Date(this.advertisement.expirationTime);
    return start<end && end<=expiration && expiration>=now
  }
}
