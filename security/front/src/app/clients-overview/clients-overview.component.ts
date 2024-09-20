import { Component, OnInit } from '@angular/core';
import { ProfileService } from '../profile/profile.service';
import { User } from '../models/user.model';
import { KeyValuePipe } from '@angular/common';
@Component({
  selector: 'app-clients-overview',
  templateUrl: './clients-overview.component.html',
  styleUrls: ['./clients-overview.component.css']
})
export class ClientsBlockingOverviewComponent implements OnInit{
  constructor(private profileService: ProfileService) { }
  users:User[]

  ngOnInit(): void {
    this.profileService.getAll().subscribe(data=>{
      this.users=data;
      console.log(this.users)
    })
  }

  onBlock(user:User):void{
    this.profileService.block(user.email).subscribe(data=>{
      this.users=data;
    })

  }

  onUnblock(user:User):void{
    this.profileService.unblock(user.email).subscribe(data=>{
      this.users=data;
    })
  }

}
