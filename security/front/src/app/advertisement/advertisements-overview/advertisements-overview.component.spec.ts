import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdvertisementsOverviewComponent } from './advertisements-overview.component';

describe('AdvertisementsOverviewComponent', () => {
  let component: AdvertisementsOverviewComponent;
  let fixture: ComponentFixture<AdvertisementsOverviewComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [AdvertisementsOverviewComponent]
    });
    fixture = TestBed.createComponent(AdvertisementsOverviewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
