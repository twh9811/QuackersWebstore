import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ProductModifyComponent } from './product-modify.component';

describe('ProductModifyComponent', () => {
  let component: ProductModifyComponent;
  let fixture: ComponentFixture<ProductModifyComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ProductModifyComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ProductModifyComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
