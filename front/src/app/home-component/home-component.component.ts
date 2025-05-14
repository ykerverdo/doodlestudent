import { Component, OnInit, Renderer2 } from '@angular/core';
import { CardSmallComponentComponent } from '../card-small-component/card-small-component.component';
import { Card } from './Card';

@Component({
  selector: 'app-home-component',
  templateUrl: './home-component.component.html',
  styleUrls: ['./home-component.component.css']
})
export class HomeComponentComponent implements OnInit {
  isVersionB: boolean = false; // Default to false

  constructor(private renderer: Renderer2) {}

  cards: Card[] = [];

  ngOnInit(): void {
    this.isVersionB = Math.random() < 0.5; // 50% chance for Version B

    // Log version for tracking
    fetch('/api/abtest/log', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ version: this.isVersionB ? 'B' : 'A', event: 'page_access' })
    });

    this.cards.push(new Card('assets/1.png', { backgroundColor: '#44baf2', color: 'white' }, 'Créez un sondage', 'Définissez plusieurs créneaux pour votre réunion.'));
    this.cards.push(new Card('assets/2.png', { backgroundColor: '#fc506d', color: 'white' }, 'Envoyez vos invitations', 'Les participants aux sondages pourront voter pour les dates qui leur conviennent le mieux !'));
    this.cards.push(new Card('assets/3.png', { backgroundColor: '#8f3ee8', color: 'white' }, 'Faites votre choix', 'Vous pourrez obtenir en direct les résultats du sondage afin de choisir au mieux la meilleure proposition.'));
  }

  onButtonClick(): void {
    // Log click event
    fetch('/api/abtest/log', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ version: this.isVersionB ? 'B' : 'A', event: 'click' })
    });
  }
}
