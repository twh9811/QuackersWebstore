import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ChangePageTestComponent } from './change-page-test.component';

describe('ChangePageTestComponent', () => {
  let component: ChangePageTestComponent;
  let fixture: ComponentFixture<ChangePageTestComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ChangePageTestComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ChangePageTestComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
