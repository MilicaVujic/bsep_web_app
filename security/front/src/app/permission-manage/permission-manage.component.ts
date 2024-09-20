import { Component, OnInit } from '@angular/core';
import { Permission } from '../models/permission.model';
import { PermissionService } from './permission.service';
import { AuthService } from '../login/auth.service';

@Component({
  selector: 'app-permission-manage',
  templateUrl: './permission-manage.component.html',
  styleUrls: ['./permission-manage.component.css']
})
export class PermissionManageComponent implements OnInit{
  visiblePermissions:boolean=false;
  unexistingPermissions:Permission[]
  existingPermissions:Permission[]
  role:string='';
  constructor(private service: PermissionService,private authService:AuthService) {}
  ngOnInit(): void {
    
  }
  
  onRoleAdmin():void{
    this.visiblePermissions=true;
    this.service.getExisting("ROLE_ADMIN").subscribe({
      next: (result: Permission[]) => {
        this.existingPermissions = result;
        this.service.getUnexisting("ROLE_ADMIN").subscribe({
          next: (result: Permission[]) => {
            this.unexistingPermissions = result;
            this.role='ROLE_ADMIN';
            
          },
          error:(error) => {
            if (error.status === 401) {
              alert(
                "Unauthorized. Maybe you should refresh your access token. "
              );
            }
          }
        })
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
  onRoleEmployee():void{
    
    this.visiblePermissions=true;
    this.service.getExisting("ROLE_EMPLOYEE").subscribe({
      next: (result: Permission[]) => {
        this.existingPermissions = result;
        this.service.getUnexisting("ROLE_EMPLOYEE").subscribe({
          next: (result: Permission[]) => {
            this.unexistingPermissions = result;
            this.role='ROLE_EMPLOYEE';
          },
          error:(error) => {
            if (error.status === 401) {
              alert(
                "Unauthorized. Maybe you should refresh your access token. "
              );
            }
          }
        })
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
  onRoleClient():void{
    
    this.visiblePermissions=true;
    this.service.getExisting("ROLE_CLIENT").subscribe({
      next: (result: Permission[]) => {
        this.existingPermissions = result;
        this.service.getUnexisting("ROLE_CLIENT").subscribe({
          next: (result: Permission[]) => {
            this.unexistingPermissions = result;
            this.role='ROLE_CLIENT';
          },
          error:(error) => {
            if (error.status === 401) {
              alert(
                "Unauthorized. Maybe you should refresh your access token. "
              );
            }
          }
        })
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
  onAdd(p:Permission):void{
    this.service.addPermission(p.name,this.role).subscribe({
      next: (result: Permission) => {
        this.visiblePermissions=true;
      this.service.getExisting(this.role).subscribe({
        next: (result: Permission[]) => {
          this.existingPermissions = result;
          this.service.getUnexisting(this.role).subscribe({
            next: (result: Permission[]) => {
              this.unexistingPermissions = result;
              
            },
            error:(error) => {
              if (error.status === 401) {
                alert(
                  "Unauthorized. Maybe you should refresh your access token. "
                );
              }
            }
          })
      },
      error:(error) => {
        if (error.status === 401) {
          alert(
            "Unauthorized. Maybe you should refresh your access token. "
          );
        }
      }
    })
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
  onRemove(p:Permission):void{
    this.service.removePermission(p.name,this.role).subscribe({
      next: (result: Permission) => {
        this.visiblePermissions=true;
      this.service.getExisting(this.role).subscribe({
        next: (result: Permission[]) => {
          this.existingPermissions = result;
          this.service.getUnexisting(this.role).subscribe({
            next: (result: Permission[]) => {
              this.unexistingPermissions = result;
              
            },
            error:(error) => {
              if (error.status === 401) {
                alert(
                  "Unauthorized. Maybe you should refresh your access token. "
                );
              }
            }
          })
      },
      error:(error) => {
        if (error.status === 401) {
          alert(
            "Unauthorized. Maybe you should refresh your access token. "
          );
        }
      }
    })
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
}
