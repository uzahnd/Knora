(function(global) {

    // map tells the System loader where to look for things
    var map = {
        'app': 'app', // 'dist',
        'rxjs': 'node_modules/rxjs',
        'angular2-in-memory-web-api': 'node_modules/angular2-in-memory-web-api',
        '@angular': 'node_modules/@angular',
        '@angular2-material': 'node_modules/@angular2-material',
        'ng2-page-scroll': 'node_modules/ng2-page-scroll',


    };

    // packages tells the System loader how to load when no filename and/or no extension
    var packages = {
        'app': {
            main: 'main.js',
            defaultExtension: 'js'
        },
        'rxjs': {
            defaultExtension: 'js'
        },
        'angular2-in-memory-web-api': {
            defaultExtension: 'js'
        },
        'ng2-page-scroll': {
            main: 'ng2-page-scroll.js',
            defaultExtension: 'js'
        },
        '@angular2-material/core': {
          format: 'cjs',
          defaultExtension: 'js',
          main: 'core.js'
        },
        '@angular2-material/toolbar': {
          format: 'cjs',
          defaultExtension: 'js',
          main: 'toolbar.js'
        },
        '@angular2-material/button': {
          format: 'cjs',
          defaultExtension: 'js',
          main: 'button.js'
        },
        '@angular2-material/icon': {
          format: 'cjs',
          defaultExtension: 'js',
          main: 'icon.js'
        },
        '@angular2-material/input': {
          format: 'cjs',
          defaultExtension: 'js',
          main: 'input.js'
        },
        '@angular2-material/grid-list': {
          format: 'cjs',
          defaultExtension: 'js',
          main: 'grid-list.js'
        },
        '@angular2-material/card': {
          format: 'cjs',
          defaultExtension: 'js',
          main: 'card.js'
        },

    };

    var packageNames = [
        '@angular/common',
        '@angular/compiler',
        '@angular/core',
        '@angular/http',
        '@angular/platform-browser',
        '@angular/platform-browser-dynamic',
        '@angular/router',
        '@angular/testing',
        '@angular/upgrade',
        '@angular2-material/core',
        '@angular2-material/toolbar',
        '@angular2-material/button',
        '@angular2-material/icon',
        '@angular2-material/input',
        '@angular2-material/grid-list',
        '@angular2-material/card',
    ];
    /*
    '@angular2-material/checkbox',
    '@angular2-material/input',
    '@angular2-material/list',
    '@angular2-material/progress-bar',
    '@angular2-material/progress-circle',
    '@angular2-material/radio',
    '@angular2-material/sidenav',
    */

    // add package entries for angular packages in the form '@angular/common': { main: 'index.js', defaultExtension: 'js' }
    packageNames.forEach(function(pkgName) {
        packages[pkgName] = {
            main: 'index.js',
            defaultExtension: 'js'
        };
    });

    var config = {
        map: map,
        packages: packages
    };

    // filterSystemConfig - index.html's chance to modify config before we register it.
    if (global.filterSystemConfig) {
        global.filterSystemConfig(config);
    }

    System.config(config);

})(this);
