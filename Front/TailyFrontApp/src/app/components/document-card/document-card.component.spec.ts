import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DocumentCardComponent } from './document-card.component';

describe('DocumentCardComponent', () => {
  let component: DocumentCardComponent;
  let fixture: ComponentFixture<DocumentCardComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [DocumentCardComponent]
    });
    fixture = TestBed.createComponent(DocumentCardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
