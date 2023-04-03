import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PaymentModifyComponent } from './payment-modify.component';

describe('PaymentModifyComponent', () => {
  let component: PaymentModifyComponent;
  let fixture: ComponentFixture<PaymentModifyComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ PaymentModifyComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PaymentModifyComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
