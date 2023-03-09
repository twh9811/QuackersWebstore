import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminTestComponent } from './admin-test.component';

describe('AdminTestComponent', () => {
  let component: AdminTestComponent;
  let fixture: ComponentFixture<AdminTestComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AdminTestComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AdminTestComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
