import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { AdvertisementService } from '../advertisement.service';
import { Advertisement } from 'src/app/models/advertisement.model';

@Component({
  selector: 'app-advertisement-details',
  templateUrl: './advertisement-details.component.html',
  styleUrls: ['./advertisement-details.component.css']
})
export class AdvertisementDetailsComponent implements OnInit {
  advertisement: Advertisement;

  constructor(
    private route: ActivatedRoute,
    private advertisementService: AdvertisementService
  ) { }

  ngOnInit(): void {
    const idParam = this.route.snapshot.paramMap.get('id');
    if (idParam) {
      const id = +idParam;
      this.advertisementService.getAdvertisementById(id).subscribe((ad) => {
        this.advertisement = ad;
      });
    } else {
      console.error('ID parameter is missing in the route.');
    }
  }
}
