#!/usr/bin/env python3

import requests, json

#
# This scripts tests the creation of a reference.
#
# dump: s-get http://localhost:3030/knora-test/data http://www.knora.org/data/ssrq > knora_ssrq_dump.ttl

base_url = "http://localhost/v1/"

def knora_api_post(route, json_params):
    try:
        props = json.dumps(json_params)
        response = requests.post(base_url + route,
                          data=props,
                          headers={'content-type': 'application/json; charset=utf8'},
                          auth=('root', 'test'),
                          proxies={'http': 'http://localhost:3333'})

        response.raise_for_status()
        # print(response.text)
        return response
    except Exception as e:
        print(e)
        print(response.text)


external_ref_params = {
    'restype_id': 'http://www.knora.org/ontology/ssrq#externalRef',
    'properties': {
        'http://www.knora.org/ontology/ssrq#refID': [
            {'richtext_value': {'utf8str': '11111', 'textattr': json.dumps({}), 'resource_reference': []}}
        ],
        'http://www.knora.org/ontology/ssrq#refType': [
            {'hlist_value': 'http://data.knora.org/ssrq/lists/HLS'}
        ]
    },
    'label': 'ext_ref',
    'project_id': 'http://data.knora.org/projects/ssrq'
}

external_ref_response = knora_api_post('resources', external_ref_params)
external_ref_data = json.loads(external_ref_response.text)
external_ref_iri = external_ref_data['res_id']
print('Created externalRef with IRI', external_ref_iri)

link_params = {
    'project_id': 'http://data.knora.org/projects/ssrq',
    'prop': 'http://www.knora.org/ontology/ssrq#ref',
    'res_id': 'http://data.knora.org/ssrq/org000004',
    'link_value': external_ref_iri
}

props = json.dumps(link_params)

link_response = knora_api_post('values', link_params)
link_response_data = json.loads(link_response.text)
link_value_iri = link_response_data['value']
print('Created link from http://data.knora.org/ssrq/org000004 to', external_ref_iri, 'with link value IRI', link_value_iri)
