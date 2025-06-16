import { Component, OnInit, ViewChild } from '@angular/core';
import { MatPaginator, PageEvent, MatPaginatorModule } from '@angular/material/paginator';
import { DocumentDto } from 'src/app/DTOs/DocumentDto'; // Предполагается, что этот DTO уже создан
import { DocumentService } from 'src/app/services/document.service';

@Component({
  selector: 'app-document-list',
  templateUrl: './document-list.component.html',
  styleUrls: ['./document-list.component.scss'],
})
export class DocumentListComponent implements OnInit {
  allDocuments: DocumentDto[] = []; //загружаются из API
  paginatedDocuments: DocumentDto[] = [];

  pageSize = 5;
  pageIndex = 0;
  totalElements = 0;
  filterValue: string = "";

  @ViewChild(MatPaginator) paginator!: MatPaginator;

  constructor(private documentService: DocumentService) {

  }

  ngOnInit(): void {
    console.log('DocumentListComponent initialized');
    this.fetchDocuments();
  }

  ngAfterViewInit() {
        //     if (this.paginator) {
        //   this.paginator.length = this.totalElements;
        // }
  }

  fetchDocuments(): void {
    const creatorId = 2;
    const title = this.filterValue || '';
    const page = this.pageIndex;
    const size = this.pageSize;
    const sortBy = 'createdAt';
    const sortDir: 'ASC' | 'DESC' = 'ASC';

    this.documentService
      .getCreatedDocuments(creatorId, title, page, size, sortBy, sortDir)
      .subscribe((response) => {
        debugger
        this.allDocuments = response.content;
        this.totalElements = response.totalElements;
        console.log('Fetched documents:', this.allDocuments);
        debugger
        this.updatePage();
      });
  }


  updatePage(): void {
    debugger
    const start = this.pageIndex * this.pageSize;
    const end = start + this.pageSize;
    debugger
    this.paginatedDocuments = this.allDocuments.slice(start, end);
  }

  applyFilter(event: Event) {
    this.filterValue = (event.target as HTMLInputElement).value;
    console.log(this.filterValue);
    this.fetchDocuments();

    //this.paginator.firstPage();
  }

  onPageChange(event: PageEvent): void {
    this.pageIndex = event.pageIndex;
    this.pageSize = event.pageSize;
    this.fetchDocuments();
  }
}
