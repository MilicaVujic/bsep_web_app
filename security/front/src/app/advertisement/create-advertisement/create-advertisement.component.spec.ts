import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CreateAdvertisementComponent } from './create-advertisement.component';

describe('CreateAdvertisementComponent', () => {
  let component: CreateAdvertisementComponent;
  let fixture: ComponentFixture<CreateAdvertisementComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CreateAdvertisementComponent]
    });
    fixture = TestBed.createComponent(CreateAdvertisementComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
