import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdvertisementDetailsComponent } from './advertisement-details.component';

describe('AdvertisementDetailsComponent', () => {
  let component: AdvertisementDetailsComponent;
  let fixture: ComponentFixture<AdvertisementDetailsComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [AdvertisementDetailsComponent]
    });
    fixture = TestBed.createComponent(AdvertisementDetailsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
