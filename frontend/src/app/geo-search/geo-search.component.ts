import { Component, OnInit } from '@angular/core';
import { ResultPage } from '../core/models/response/result.page.model';
import { SearchService } from '../core/services/search.service';

@Component({
  selector: 'app-geo-search',
  templateUrl: './geo-search.component.html',
  styleUrls: ['./geo-search.component.scss']
})
export class GeoSearchComponent implements OnInit {

  page = 0;
  size = 3;
  place = '';
  radius = 0;

  results: ResultPage = { totalElements: 0, content: [] };

  constructor(
    private searchService: SearchService,
  ) { }

  ngOnInit(): void {
  }

  public search() {
    this.page = 0;

    if (this.place == '' || this.radius == null || this.radius == 0) {
      return;
    }

    this.searchService.geoSearch(this.page, this.size, this.place, this.radius).subscribe((data: any) => {
      this.results = data;
      console.log(data);
    },
      error => {
        this.results = { totalElements: 0, content: [] };
        console.log(error.error);
      });
  }

  handlePageChange($event: number): void {
    this.page = $event;
    this.searchService.geoSearch(this.page - 1, this.size, this.place, this.radius).subscribe((data: any) => {
      this.results = data;
      console.log(data);
    },
      error => {
        console.log(error.error);
      });
  }

}
