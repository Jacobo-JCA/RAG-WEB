import { Component } from "@angular/core";

@Component({
    template: `
        <h1>Aumentar</h1>
        <h1>Counter: {{ counter }}</h1>
        <button (click)="increaseBy(1)">+1</button>
        <button (click)="decreaseBy(1)">-1</button>
        <button (click)="resetBy()">Reset</button>
    `
})
export class CounterPageComponent {
    counter = 15;
    increaseBy(value: number) {
        this.counter += value;
    }

    decreaseBy(value: number) {
        this.counter -= value;
    }

    resetBy() {
        this.counter = 10;
    }

}