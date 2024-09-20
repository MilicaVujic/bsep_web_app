import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Advertisement } from 'src/app/models/advertisement.model';
import { AdvertisementService } from '../advertisement.service';
import { DatePipe } from '@angular/common';
import { AdvertisementRequest } from 'src/app/models/advertisement-request.model';
import { AppComponent } from 'src/app/app.component';
import { AuthService } from 'src/app/login/auth.service';
import {format} from 'date-fns'
import { SanitizeService } from 'src/app/sanitize.service';

@Component({
  selector: 'app-create-advertisement',
  templateUrl: './create-advertisement.component.html',
  styleUrls: ['./create-advertisement.component.css'],
})
export class CreateAdvertisementComponent implements OnInit {
  constructor(
    private route: ActivatedRoute,
    private service: AdvertisementService,
    private datePipe: DatePipe,
    private appComponent:AppComponent,
    private authService: AuthService,
    private router:Router,
    private sanitizeService:SanitizeService
  ) {}
  id: number;
  request: AdvertisementRequest;
  currentDateTime: Date = new Date();
  ngOnInit(): void {
    this.route.params.subscribe((params) => {
      const requestId = params['id'];
      this.service.getRequestById(requestId).subscribe({
        next: (result) => {
          console.log('Dobavljen!', result);
          this.request = result;
          this.service.getClientId(this.request.id).subscribe({
            next: (result) => {
              console.log('client id!', result);
              this.id = result;
              this.advertisement.clientId = result;
              this.advertisement.requestId = requestId;
            },
            error:(error) => {
              if (error.status === 401) {
                alert(
                  "Unauthorized. Maybe you should refresh your access token. "
                );
              }
            }
          });
        },
        error:(error) => {
          if (error.status === 401) {
            alert(
              "Unauthorized. Maybe you should refresh your access token. "
            );
          }
        }
      });
    });
  }
  advertisement: Advertisement = {
    id: 0,
    slogan: '',
    clientId: 0,
    description: '',
    duration: '',
    requestId: 0,
  };
  submit(): void {
    var hasPermission = false;
    var permissions = this.authService.getRolePermissions();
    permissions?.forEach(p => {
      if(p=="CREATE_ADV") {
        hasPermission = true;
        
      }

    })
    if(hasPermission){
      this.advertisement.slogan=this.sanitizeService.sanitizeInput(this.advertisement.slogan)
      this.advertisement.description=this.sanitizeService.sanitizeInput(this.advertisement.description)
      this.advertisement.duration=this.sanitizeService.sanitizeInput(this.advertisement.duration)
        if(this.advertisement.slogan!='' && this.advertisement.description!='' && this.advertisement.duration!=''){
          this.service
          .createAdvertisement(
            this.advertisement,
            this.id,
            this.advertisement.requestId
          )
          .subscribe({
            next: (result) => {
              if(result==null){
                alert("Invalid data.")
                this.router.navigate(['advertisement-request'])
              }
              else {
                console.log('Created!', result);
                alert('Created.')
                this.router.navigate(['advertisements'])
              }
              
            },
            error:(error) => {
              if (error.status === 401) {
                alert(
                  "Unauthorized. Maybe you should refresh your access token. "
                );
              }
            }
          });

        }else{
            alert("Invalid fields.")
        }
      } else{
        alert('No permission.');
      }

    }
    
  }
