import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DucksearchComponent } from './ducksearch.component';

describe('DucksearchComponent', () => {
  let component: DucksearchComponent;
  let fixture: ComponentFixture<DucksearchComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DucksearchComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DucksearchComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
