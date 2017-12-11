#!/usr/bin/groovy
def call(String namespace) {

    try {
        sh "kubectl create namespace ${namespace} || true"
    } catch (err) {
        echo "Cannot create namespace"
        echo "Exception: ${err}"
        throw err
    }
}