from IPython.core.magic import Magics, cell_magic, magics_class
import requests, json, time
import os
from pprint import pprint

@magics_class
class RumbleDBMagic(Magics):
    def spark_submit(self, host, command, file):
        data = {
            'file': file,
            'className': 'org.rumbledb.cli.Main',
            'args': ['--master', 'yarn-cluster', '--query', command, '--show-error-info', 'yes']
        }
        headers = {'Content-Type': 'application/json'}
        response = requests.post(host + '/batches', data = json.dumps(data), headers=headers)
        return response.headers['Location']

    @cell_magic
    def rumbledb_livy(self, line, cell=None):
        if cell is None:
            data = line
        else:
            data = cell
        
        start = time.time()
        location = self.spark_submit(os.environ["RUMBLEDB_HOST"], data, os.environ["RUMBLEDB_JAR_PATH"])
        r = requests.get(os.environ["RUMBLEDB_HOST"] + location, data=json.dumps({'kind': 'spark'}), headers={'Content-Type': 'application/json'})
        while True:
            r = requests.get(os.environ["RUMBLEDB_HOST"] + location + '/state', data=json.dumps({'kind': 'spark'}), headers={'Content-Type': 'application/json'})
            state = r.json()['state']
            if state == 'success' or state == 'dead':
                break
            time.sleep(0.1)
        end = time.time()
        r = requests.get(os.environ["RUMBLEDB_HOST"] + location + '/log', data=json.dumps({'kind': 'spark'}), params={'from': 0, 'size': 99999}, headers={'Content-Type': 'application/json'})
        response = r.json()
        print("Took: %s ms" % (end - start))
        pprint(response['log'][1:-1])
    
    @cell_magic
    def rumbledb_server(self, line, cell=None):
        if cell is None:
            data = line
        else:
            data = cell

        start = time.time()                                                         
        response = json.loads(requests.post(os.environ["RUMBLEDB_SERVER"], data=data).text)                   
        end = time.time()                                                              
        print("Took: %s ms" % (end - start))

        if 'warning' in response:
            print(json.dumps(response['warning']))
        if 'values' in response:
            for e in response['values']:
                print(json.dumps(e))
        elif 'error-message' in response:
            return print(response['error-message'])
        else:
            return print(response)

def load_ipython_extension(ipython):
    ipython.register_magics(RumbleDBMagic)
