import { Component, OnInit } from '@angular/core';
import { ResultPage } from '../core/models/response/result.page.model';
import { SearchService } from '../core/services/search.service';

@Component({
  selector: 'app-simple-search',
  templateUrl: './simple-search.component.html',
  styleUrls: ['./simple-search.component.scss']
})
export class SimpleSearchComponent implements OnInit {
  page = 0;
  size = 3;
  searchField = '';
  searchParams = [];
  visitedIds = [];


  results: ResultPage = { totalElements: 0, content: [] };

  constructor(
    private searchService: SearchService,
  ) { }

  ngOnInit(): void {
    this.visitedIds = JSON.parse(localStorage.getItem('visitedLinks'));
  }

  public search() {
    this.page = 0;
    if (this.searchField == "") {
      return;
    }

    this.searchService.search(this.searchParams, this.page, this.size, this.searchField).subscribe((data: any) => {
      this.results = data;
      console.log(data);
    },
      error => {
        console.log(error.error);
      });
  }

  handlePageChange($event: number): void {
    this.page = $event;
    this.searchService.search(this.searchParams, this.page - 1, this.size, this.searchField).subscribe((data: any) => {
      this.results = data;
      console.log(data);
    },
      error => {
        console.log(error.error);
      });
  }

}
