import { Component, Input } from '@angular/core';
import { Duck, DuckOutfit } from '../duck';

@Component({
  selector: 'app-duck-image',
  templateUrl: './duck-image.component.html',
  styleUrls: ['./duck-image.component.css']
})
export class DuckImageComponent {

  @Input() duck = <Duck>{};

  /**
   * Gets the path to the base image for a duck 
   * 
   * @param duck The duck
   * @returns The path to the image
   */
  getDuckColorImage(): string {
    if (this.duck.size == "EXTRA_LARGE") return "";

    const color = this.duck.color.toLowerCase();
    const colorFile = color.charAt(0).toUpperCase() + color.slice(1);
    return `/assets/duck-colors/${this.duck.size}/${colorFile}.png`;
  }

  /**
   * Gets the path to a given accessory's image
   * 
   * @param accessoryName The name of the accessory
   * @param duck The duck 
   * @returns The path to the image
   */
  getAccessoryImage(accessoryName: string): string {
    const outfit: any = this.duck.outfit;
    if (outfit[accessoryName + "UID"] == 0) return "";

    return `/assets/duck-${accessoryName}/${outfit[accessoryName + "UID"]}.png`;
  }

  /**
   * Gets the css class for a given duck accessory
   * 
   * @param accessoryName The name of the accessory
   * @param duck The duck
   * @returns The name of the css class for the accessory
   */
  getCSSClass(accessoryName: string): string {
    const outfit: any = this.duck.outfit;
    return `duck-${accessoryName}-${outfit[accessoryName + "UID"]}-${this.duck.size.toLowerCase()}`;
  }

}
