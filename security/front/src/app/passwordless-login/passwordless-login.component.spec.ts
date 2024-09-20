import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PasswordlessLoginComponent } from './passwordless-login.component';

describe('PasswordlessLoginComponent', () => {
  let component: PasswordlessLoginComponent;
  let fixture: ComponentFixture<PasswordlessLoginComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PasswordlessLoginComponent]
    });
    fixture = TestBed.createComponent(PasswordlessLoginComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
