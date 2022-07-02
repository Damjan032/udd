import { Component, OnInit } from '@angular/core';
import { ResultPage } from '../core/models/response/result.page.model';
import { SearchService } from '../core/services/search.service';

@Component({
  selector: 'app-advanced-search',
  templateUrl: './advanced-search.component.html',
  styleUrls: ['./advanced-search.component.scss']
})
export class AdvancedSearchComponent implements OnInit {
  page = 0;
  size = 3;
  paramList = [
    {
      paramNamePreview: 'First name',
      paramName: 'firstName',
      paramValue: '',
      paramValue2: '',
      paramType: 'AND'
    }
  ]
  searchParams = [];

  results: ResultPage = { totalElements: 0, content: [] };

  constructor(
    private searchService: SearchService,
  ) { }

  ngOnInit(): void {
  }

  public search() {
    this.searchParams = [];
    this.page = 0;

    if (this.paramList.length > 1) {
      this.paramList[0].paramType = this.paramList[1].paramType;
    }

    for (let index = 0; index < this.paramList.length; index++) {
      let paramName = this.paramList[index].paramName;
      let paramValue = this.paramList[index].paramValue;
      let paramValue2 = this.paramList[index].paramValue2;
      let paramType = this.paramList[index].paramType;
      let phraseQuery = this.paramList[index].paramValue[0] == "\"" && this.paramList[index].paramValue[this.paramList[index].paramValue.length - 1] == "\"";
      if (paramName != "educationDegree") {
        this.searchParams.push({
          "attributeName": paramName,
          "searchValue": paramValue,
          "phraseQuery": phraseQuery,
          "type": paramType
        });
      } else {
        this.searchParams.push({
          "attributeName": paramName,
          "searchValue": paramValue,
          "searchValue2": paramValue2,
          "phraseQuery": phraseQuery,
          "type": paramType
        });
      }
    }

    if (this.searchParams.length == 0) {
      return;
    }

    this.searchService.advancedSearch(this.searchParams, this.page, this.size).subscribe((data: any) => {
      this.results = data;
      console.log(data);
    },
      error => {
        console.log(error.error);
      });
  }

  handlePageChange($event: number): void {
    this.page = $event;
    this.searchService.advancedSearch(this.searchParams, this.page - 1, this.size).subscribe((data: any) => {
      this.results = data;
      console.log(data);
    },
      error => {
        console.log(error.error);
      });
  }

  addField(): void {
    this.paramList.push({
      paramNamePreview: 'First name',
      paramName: 'firstName',
      paramValue: '',
      paramValue2: '',
      paramType: 'AND'
    });
  }

  remove(index: any): void {
    this.paramList.splice(index, 1);
  }

  paramChanged(index: any): void {
    if (this.paramList[index].paramName == 'firstName') {
      this.paramList[index].paramNamePreview = 'First name'
    } else if (this.paramList[index].paramName == 'lastName') {
      this.paramList[index].paramNamePreview = 'Last name'
    } else if (this.paramList[index].paramName == 'cv') {
      this.paramList[index].paramNamePreview = 'CV content'
    } else if (this.paramList[index].paramName == 'educationDegree') {
      this.paramList[index].paramValue = '1 - Less than a high school diploma'
      this.paramList[index].paramValue2 = '1 - Less than a high school diploma'
    }
  }
}
