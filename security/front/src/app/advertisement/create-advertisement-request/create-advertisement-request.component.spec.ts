import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CreateAdvertisementRequestComponent } from './create-advertisement-request.component';

describe('CreateAdvertisementRequestComponent', () => {
  let component: CreateAdvertisementRequestComponent;
  let fixture: ComponentFixture<CreateAdvertisementRequestComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CreateAdvertisementRequestComponent]
    });
    fixture = TestBed.createComponent(CreateAdvertisementRequestComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
