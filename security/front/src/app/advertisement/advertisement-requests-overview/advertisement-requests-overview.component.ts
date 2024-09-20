import { Component, OnInit } from '@angular/core';
import { AdvertisementRequest, RequestStatus } from 'src/app/models/advertisement-request.model';
import { AdminProfileComponent } from 'src/app/profile/admin-profile/admin-profile.component';
import { AdvertisementService } from '../advertisement.service';
import { Route, Router } from '@angular/router';
import { AuthService } from 'src/app/login/auth.service';
import { AppComponent } from 'src/app/app.component';

@Component({
  selector: 'app-advertisement-requests-overview',
  templateUrl: './advertisement-requests-overview.component.html',
  styleUrls: ['./advertisement-requests-overview.component.css']
})
export class AdvertisementRequestsOverviewComponent implements OnInit{
  advertisementRequests: AdvertisementRequest[] = [];

  constructor(private service: AdvertisementService,private router:Router, private appComponent:AppComponent, private authService: AuthService) { }
  
  ngOnInit(): void {
    this.loadAdvertisementRequests();
  }

  loadAdvertisementRequests(): void {
    var hasPermission = false;
    var permissions = this.authService.getRolePermissions();
    permissions?.forEach(p => {
      if(p=="REQ_VIEW") {
        hasPermission = true;
        
      }

    })
    if(hasPermission){
      this.service.getAllRequests().subscribe(
        (data: AdvertisementRequest[]) => {
          this.advertisementRequests = data;
        console.log(data);
          console.log(data);
        },
        (error) => {
          if (error.status === 401) {
            alert(
              "Unauthorized. Maybe you should refresh your access token. "
            );
          }
          console.error('Error loading advertisement requests:', error);
        }
      );
    }else{
      alert('No permission.');
    }
    }
    
  openForm(id:number):void{
    this.router.navigate(['create-advertisement',id]);

  }

}
