import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DuckImageComponent } from './duck-image.component';

describe('DuckImageComponent', () => {
  let component: DuckImageComponent;
  let fixture: ComponentFixture<DuckImageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DuckImageComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DuckImageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
