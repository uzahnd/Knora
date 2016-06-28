#!/usr/bin/env python3

import requests, json

#
# This scripts tests the creation of a residence.
#

try:
    base_url = "http://localhost/v1/"

    params = {
        'restype_id': 'http://www.knora.org/ontology/ssrq#residence',
        'properties': {
            'http://www.knora.org/ontology/ssrq#residenceID': [
                {'richtext_value': {'utf8str': 'loc000109', 'textattr': json.dumps({}), 'resource_reference': []}}
            ],
            'http://www.knora.org/ontology/ssrq#stdName': [
                {'richtext_value': {'utf8str': 'Zürich', 'textattr': json.dumps({}), 'resource_reference': []}}
            ]
        },
        'label': 'test residence',
        'project_id': 'http://data.knora.org/projects/ssrq'
    }

    props = json.dumps(params)

    r = requests.post(base_url + 'resources',
                      data=props,
                      headers={'content-type': 'application/json; charset=utf8'},
                      auth=('root', 'test'),
                      proxies={'http': 'http://localhost:3333'})

    r.raise_for_status()
    print(r.text)
except Exception as e:
    print('Knora API answered with an error:\n')
    print(e)
    print(r.text)
