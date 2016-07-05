#!/usr/bin/env python3

import requests, json

#
# This scripts tests the creation of a reference.
#
# dump: s-get http://localhost:3030/knora-test/data http://www.knora.org/data/ssrq > knora_ssrq_dump.ttl

try:
    base_url = "http://localhost/v1/"

#    params = {
#        'restype_id': 'http://www.knora.org/ontology/ssrq#externalRef',
#        'properties': {
#            'http://www.knora.org/ontology/ssrq#refID': [
#                {'richtext_value': {'utf8str': '11111', 'textattr': json.dumps({}), 'resource_reference': []}}
#            ],
#            'http://www.knora.org/ontology/ssrq#refType': [
#                {'hlist_value': 'http://data.knora.org/ssrq/lists/HLS'}
#            ]
#        },
#        'label': 'ext_ref',
#        'project_id': 'http://data.knora.org/projects/ssrq'
#    }
    params = {
    
        'project_id': 'http://data.knora.org/projects/ssrq', 
        'prop': 'http://www.knora.org/ontology/ssrq#ref',
        'res_id': 'http://data.knora.org/ssrq/org000004', 
        
        'link_value': 'http://data.knora.org/Aps-JodlTVmDhSTCD4cETg' ,
        
        
    }

    props = json.dumps(params)
#    r = requests.post(base_url + 'resources',
    r = requests.post(base_url + 'values', 
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
