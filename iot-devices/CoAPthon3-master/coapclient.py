#!/usr/bin/env python
import getopt
import socket
import sys

from coapthon.client.helperclient import HelperClient
from coapthon.utils import parse_uri
# 
# __author__ = 'Giacomo Tanganelli'

client = None


def usage():  # pragma: no cover
    print("Command:\tcoapclient.py -o -p [-P]")
    print("Options:")
    print("\t-o, --operation=\tGET|PUT|POST|DELETE|DISCOVER|OBSERVE")
    print("\t-p, --path=\t\t\tPath of the request")
    print("\t-P, --payload=\t\tPayload of the request")
    print("\t-f, --payload-file=\t\tFile with payload of the request")


def client_callback(response):
    print("Callback")


def client_callback_observe(response):  # pragma: no cover
    global client
    print("Callback_observe")
    check = True
    while check:
        chosen = eval(input("Stop observing? [y/N]: "))
        if chosen != "" and not (chosen == "n" or chosen == "N" or chosen == "y" or chosen == "Y"):
            print("Unrecognized choose.")
            continue
        elif chosen == "y" or chosen == "Y":
            while True:
                rst = eval(input("Send RST message? [Y/n]: "))
                if rst != "" and not (rst == "n" or rst == "N" or rst == "y" or rst == "Y"):
                    print("Unrecognized choose.")
                    continue
                elif rst == "" or rst == "y" or rst == "Y":
                    client.cancel_observing(response, True)
                else:
                    client.cancel_observing(response, False)
                check = False
                break
        else:
            break


def main():  # pragma: no cover
    global client
#     op = None
#     path = None
#     payload = None
    op = "PUT"
    path = "coap://localhost:5683"
    host = "localhost"
    port = 5683
    payload ="zxcvbm"
    client = HelperClient(server=(host, port));
        
    if op is None:
        print("Operation must be specified")
        usage()
        sys.exit(2)

    if path is None:
        print("Path must be specified")
        usage()
        sys.exit(2)

    if not path.startswith("coap://"):
        print("Path must be conform to coap://host[:port]/path")
        usage()
        sys.exit(2)

    try:
        tmp = socket.gethostbyname(host)
        host = tmp
    except socket.gaierror:
        pass
    client = HelperClient(server=(host, port))
    if op == "GET":
        if path is None:
            print("Path cannot be empty for a GET request")
            usage()
            sys.exit(2)
        response = client.get(path)
        print((response.pretty_print()))
        client.stop()
    elif op == "OBSERVE":
        if path is None:
            print("Path cannot be empty for a GET request")
            usage()
            sys.exit(2)
        client.observe(path, client_callback_observe)
        
    elif op == "DELETE":
        if path is None:
            print("Path cannot be empty for a DELETE request")
            usage()
            sys.exit(2)
        response = client.delete(path)
        print((response.pretty_print()))
        client.stop()
    elif op == "POST":
        if path is None:
            print("Path cannot be empty for a POST request")
            usage()
            sys.exit(2)
        if payload is None:
            print("Payload cannot be empty for a POST request")
            usage()
            sys.exit(2)
        response = client.post(path, payload)
        print((response.pretty_print()))
        client.stop()
    elif op == "PUT":
        if path is None:
            print("Path cannot be empty for a PUT request")
            usage()
            sys.exit(2)
        if payload is None:
            print("Payload cannot be empty for a PUT request")
            usage()
            sys.exit(2)
        response = client.put(path, payload)
        print((response.pretty_print()))
        client.stop()
    elif op == "DISCOVER":
        response = client.discover()
        print((response.pretty_print()))
        client.stop()
    else:
        print("Operation not recognized")
        usage()
        sys.exit(2)


if __name__ == '__main__':  # pragma: no cover
    main()
