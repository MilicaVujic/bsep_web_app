import { Component } from '@angular/core';
import { User } from 'src/app/models/user.model';
import { PeopleService } from '../people.service';
import { AppComponent } from 'src/app/app.component';
import { AuthService } from 'src/app/login/auth.service';

@Component({
  selector: 'app-clients-overview',
  templateUrl: './clients-overview.component.html',
  styleUrls: ['./clients-overview.component.css']
})
export class ClientsOverviewComponent {
  clients: User[] = [];
  constructor(private service: PeopleService,private appComponent:AppComponent, private authService: AuthService) {}
  ngOnInit(): void {
    var hasPermission = false;
    var permissions = this.authService.getRolePermissions();
    permissions?.forEach(p => {
      if(p=="PEOPLE_VIEW") {
        hasPermission = true;
        
      }

    })
    if(hasPermission){
      this.service.getClients().subscribe({
        next: (result: User[]) => {
          this.clients = result;
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
      alert('No permission.');
    }
  }
}
