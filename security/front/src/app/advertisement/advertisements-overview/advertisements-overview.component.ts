import { Component, OnInit } from '@angular/core';
import { Advertisement } from 'src/app/models/advertisement.model';
import { AdvertisementService } from '../advertisement.service';
import { AuthService } from 'src/app/login/auth.service';
import { AppComponent } from 'src/app/app.component';
import { ProfileService } from 'src/app/profile/profile.service';
import { User } from 'src/app/models/user.model';
import { Router } from '@angular/router';
import { identity } from 'rxjs';

@Component({
  selector: 'app-advertisements-overview',
  templateUrl: './advertisements-overview.component.html',
  styleUrls: ['./advertisements-overview.component.css'],
})
export class AdvertisementsOverviewComponent implements OnInit {
  advertisements: Advertisement[] = [];
  user: User;
  type: string;
  constructor(
    private service: AdvertisementService,
    private appComponent: AppComponent,
    private authService: AuthService,
    private profileService: ProfileService,
    private router: Router
  ) {}
  email: string;
  ngOnInit(): void {
    var hasPermission = false;
    var permissions = this.authService.getRolePermissions();
    console.log(permissions);
    permissions?.forEach((p) => {
      if (p == 'MY_ADV' || p == 'ALL_ADV') {
        hasPermission = true;
      }
    });
    if (hasPermission) {
      if (this.appComponent.isEmployee()) this.loadAdvertisement();
      else {
        this.email = this.authService.getUser().sub;
        this.profileService.getClient(this.email).subscribe({
          next: (result: User) => {
            this.user = result;
            console.log(this.user);
            this.loadClientAdvertisement(this.user.id);
            this.getClientType(this.user.email);
          },
          error: (error) => {
            if (error.status === 401) {
              alert(
                'Unauthorized. Maybe you should refresh your access token. '
              );
            }
          },
        });
      }
    } else alert('No permission.');
  }
  getClientType(email: string): void {
    this.profileService.getClientForType(email).subscribe(
      (data: any) => {
        this.type = data.packageType.toString();
      },
      (error) => {
        if (error.status === 401) {
          alert('Unauthorized. Maybe you should refresh your access token. ');
        }

        console.error('Error loading advertisement requests:', error);
      }
    );
  }
  loadAdvertisement(): void {
    if (this.appComponent.isEmployee()) {
      this.service.getAllAdvertisements().subscribe(
        (data: Advertisement[]) => {
          this.advertisements = data;
          console.log(data);
        },
        (error) => {
          if (error.status === 401) {
            alert('Unauthorized. Maybe you should refresh your access token. ');
          }

          console.error('Error loading advertisement requests:', error);
        }
      );
    } else {
      alert('Not found');
    }
  }

  loadClientAdvertisement(id: number): void {
    if (this.appComponent.isClient()) {
      this.service.getAllAdvertisementsByUserId(id).subscribe(
        (data: Advertisement[]) => {
          this.advertisements = data;
          console.log(data);
        },
        (error) => {
          if (error.status === 401) {
            alert('Unauthorized. Maybe you should refresh your access token. ');
          }

          console.error('Error loading advertisement requests:', error);
        }
      );
    } else {
      alert('Not found');
    }
  }

  viewDetails(id: number) {
    this.service.advClick(id).subscribe(
      (res) => {
        this.router.navigate(['/advertisement-details', id]);
      },
      (err) => {
        if (err.status === 429) {
          alert('Too many requests.');
        }
      }
    );
  }
}
