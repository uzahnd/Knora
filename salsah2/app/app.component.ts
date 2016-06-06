import { Component }       from '@angular/core';

import { Routes, ROUTER_DIRECTIVES } from '@angular/router';
import { PageScroll } from 'ng2-page-scroll';

import { MdToolbar } from '@angular2-material/toolbar/toolbar';
import { MdInput } from '@angular2-material/input/input';
import { MdGridList } from '@angular2-material/grid-list/grid-list';
import { MdCard } from '@angular2-material/card/card';

// Start section
import { LoginComponent } from './components/login/login.component';

@Component({
    selector: 'landing-page',
    templateUrl: './app/app.component.html',

    directives: [ROUTER_DIRECTIVES, PageScroll, MdToolbar, MdInput, MdGridList, MdCard],
    providers: []
})

@Routes([
    { path: '/', component: LoginComponent }
])

export class AppComponent {
    title = '';
    menuitems: any = [
        {
            'name': 'features',
            'ownpage': false
        },
        {
            'name': 'documentation',
            'ownpage': true
        },
        {
            'name': 'about',
            'ownpage': false
        }
    ];
    system: any = {
        'name': 'SALSAH',
        'title': 'System for Annotation and Linkage of Sources in Arts and Humanities',
        'logo': 'theme/default/img/logo/cube-white-corner.png',
        'login': 'http://salsah.org'
    };
}
