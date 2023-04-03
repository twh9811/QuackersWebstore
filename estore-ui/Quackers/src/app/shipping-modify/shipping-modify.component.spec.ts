import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ShippingModifyComponent } from './shipping-modify.component';

describe('ShippingModifyComponent', () => {
  let component: ShippingModifyComponent;
  let fixture: ComponentFixture<ShippingModifyComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ShippingModifyComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ShippingModifyComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
