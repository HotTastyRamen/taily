import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DocumentVersionsDialogComponent } from './document-versions-dialog.component';

describe('DocumentVersionsDialogComponent', () => {
  let component: DocumentVersionsDialogComponent;
  let fixture: ComponentFixture<DocumentVersionsDialogComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [DocumentVersionsDialogComponent]
    });
    fixture = TestBed.createComponent(DocumentVersionsDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
