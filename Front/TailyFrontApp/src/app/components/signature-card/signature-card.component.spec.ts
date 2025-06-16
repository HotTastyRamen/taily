import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SignatureCardComponent } from './signature-card.component';

describe('SignatureCardComponent', () => {
  let component: SignatureCardComponent;
  let fixture: ComponentFixture<SignatureCardComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [SignatureCardComponent]
    });
    fixture = TestBed.createComponent(SignatureCardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
