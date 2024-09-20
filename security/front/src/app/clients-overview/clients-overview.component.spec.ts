import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ClientsBlockingOverviewComponent } from './clients-overview.component';

describe('ClientsOverviewComponent', () => {
  let component: ClientsBlockingOverviewComponent;
  let fixture: ComponentFixture<ClientsBlockingOverviewComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ClientsBlockingOverviewComponent]
    });
    fixture = TestBed.createComponent(ClientsBlockingOverviewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
