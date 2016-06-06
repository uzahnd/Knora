import { Component } from '@angular/core';
import { ROUTER_DIRECTIVES } from '@angular/router';
import { MD_GRID_LIST_DIRECTIVES } from '@angular2-material/grid-list/grid-list';
import { MdInput } from '@angular2-material/input/input';

import { MdCard } from '@angular2-material/card/card';

@Component({
    templateUrl: './app/components/login/login.component.html',
    directives: [ROUTER_DIRECTIVES, MD_GRID_LIST_DIRECTIVES, MdInput, MdCard]

})

export class LoginComponent {
    vm: any = {};
}
