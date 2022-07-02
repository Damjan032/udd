import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { FormControl } from '@angular/forms';

@Component({
  selector: 'app-search',
  templateUrl: './search.component.html',
  styleUrls: ['./search.component.scss']
})
export class SearchComponent implements OnInit {
  searchField: FormControl;

  @Output()
  searchChanged: EventEmitter<string> = new EventEmitter();

  constructor() {
  }

  ngOnInit(): void {
    this.searchField = new FormControl();
    this.searchField.valueChanges.subscribe(
      (userInput: string) => {
        this.searchChanged.emit(userInput);
      }
    );
  }

}
