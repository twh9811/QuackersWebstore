import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CustomerTestComponent } from './customer-test.component';

describe('CustomerTestComponent', () => {
  let component: CustomerTestComponent;
  let fixture: ComponentFixture<CustomerTestComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CustomerTestComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CustomerTestComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
