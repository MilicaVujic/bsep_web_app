import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdvertisementRequestsOverviewComponent } from './advertisement-requests-overview.component';

describe('AdvertisementRequestsOverviewComponent', () => {
  let component: AdvertisementRequestsOverviewComponent;
  let fixture: ComponentFixture<AdvertisementRequestsOverviewComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [AdvertisementRequestsOverviewComponent]
    });
    fixture = TestBed.createComponent(AdvertisementRequestsOverviewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
