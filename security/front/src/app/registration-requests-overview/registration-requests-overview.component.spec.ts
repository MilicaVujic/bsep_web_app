import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RegistrationRequestsOverviewComponent } from './registration-requests-overview.component';

describe('RegistrationRequestsOverviewComponent', () => {
  let component: RegistrationRequestsOverviewComponent;
  let fixture: ComponentFixture<RegistrationRequestsOverviewComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [RegistrationRequestsOverviewComponent]
    });
    fixture = TestBed.createComponent(RegistrationRequestsOverviewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
