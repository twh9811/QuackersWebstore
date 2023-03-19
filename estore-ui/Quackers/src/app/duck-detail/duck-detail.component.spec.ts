import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DuckDetailComponent } from './duck-detail.component';

describe('DuckDetailComponent', () => {
  let component: DuckDetailComponent;
  let fixture: ComponentFixture<DuckDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DuckDetailComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DuckDetailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
