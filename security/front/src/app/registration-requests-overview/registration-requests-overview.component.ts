import { Component, OnInit } from '@angular/core';
import { RegistrationRequest } from '../models/registrationRequest.model';
import { RegistrationService } from '../registration/registration.service';
import { AppComponent } from '../app.component';
import { AuthService } from '../login/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-registration-requests-overview',
  templateUrl: './registration-requests-overview.component.html',
  styleUrls: ['./registration-requests-overview.component.css']
})
export class RegistrationRequestsOverviewComponent implements OnInit{
  registrationRequests: RegistrationRequest[];
  explanation:"";
  explanationVisibility=false;
  rejectedRequest:RegistrationRequest;

  constructor(private router: Router, private registrationRequestService: RegistrationService,private appComponent:AppComponent, private authService: AuthService) { }

  ngOnInit(): void {
    this.getRegistrationRequests();
  }

  getRegistrationRequests(): void {
    var hasPermission = false;
    var permissions = this.authService.getRolePermissions();
    permissions?.forEach(p => {
      if(p=="ALL_REG_REQ") {
        hasPermission = true;
        
      }

    })
    if(hasPermission){
      this.registrationRequestService.getAllRegistrationRequests()
      .subscribe(registrationRequests =>{ 
        this.registrationRequests = registrationRequests;
      },
      (err) => {
        if (err.status === 401) {
          alert(
            "Unauthorized. Maybe you should refresh your access token. "
          );
        }
      }
    );
    }else{
      alert("No permission.")
    }
    
  }

  acceptRequest(request: RegistrationRequest): void {
    var hasPermission = false;
    var permissions = this.authService.getRolePermissions();
    permissions?.forEach(p => {
      if(p=="ACCEPT_REG_REQ") {
        hasPermission = true;
        
      }

    })
    if(hasPermission) {
      this.registrationRequestService.acceptRegistrationRequests(request).subscribe(registrationRequests =>{ 
        this.registrationRequests = registrationRequests;
      },
      (err) => {
        if (err.status === 401) {
          alert(
            "Unauthorized. Maybe you should refresh your access token. "
          );
        }
      }
      )
    }
    else
    alert("No permission.")
      
  }

  rejectRequest(): void {
    var hasPermission = false;
    var permissions = this.authService.getRolePermissions();
    permissions?.forEach(p => {
      if(p=="REJECT_REG_REQ") {
        hasPermission = true;
        
      }

    })

    if(hasPermission){
      this.registrationRequestService.rejectRegistrationRequests(this.rejectedRequest,this.explanation).subscribe(registrationRequests =>{ 
        this.registrationRequests = registrationRequests;
        this.explanationVisibility=false;
      },
      (err) => {
        if (err.status === 401) {
          alert(
            "Unauthorized. Maybe you should refresh your access token. "
          );
        }
      }
    )

    }
    else{
      alert("No permission.")
    }
  }
  changeExplantaionVisibility(request: RegistrationRequest):void{
    this.rejectedRequest=request;
    this.explanationVisibility=true;
  }
}
