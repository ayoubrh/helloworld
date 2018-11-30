import { Component, OnInit  } from '@angular/core';
import { Helloworld } from './helloworld';
import { Helloworlds } from './mock-helloworld';
import { HelloworldService } from './helloworld.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {
  msgs: Helloworld[];

  constructor(private helloworldService: HelloworldService) { }

  ngOnInit() {
    this.getHellowolds();
  }

  getHellowolds(): void {
    this.helloworldService.getmsgs()
        .subscribe(hellos => this.msgs = hellos);
  }

  add(message: string): void {
    message = message.trim();
    if (!message) { return; }
    this.helloworldService.addHelloWorld({ message } as Helloworld)
      .subscribe(hello => {
        this.msgs.push(hello);
      });
  }
}
