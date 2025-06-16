import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AssignSignaturesDialogComponent } from './assign-signatures-dialog.component';

describe('AssignSignaturesDialogComponent', () => {
  let component: AssignSignaturesDialogComponent;
  let fixture: ComponentFixture<AssignSignaturesDialogComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [AssignSignaturesDialogComponent]
    });
    fixture = TestBed.createComponent(AssignSignaturesDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
