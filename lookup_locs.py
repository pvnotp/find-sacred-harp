import json
from json import encoder
import urllib2
import time

KEY="AIzaSyCuMCzvNjdpzYJMFR8BWmbGzO68HbHPkGA"

def lookup_ll(loc):
  loc = loc.replace(" ", "+")
  q = "https://maps.googleapis.com/maps/api/geocode/json?address=%s&key=%s" % (loc, KEY)
  j = urllib2.urlopen(q).read()
  r = json.loads(j)

  try:
    ll = r["results"][0]["geometry"]["location"]
  except Exception:
    import pprint
    pprint.pprint(r)
    raise

  return round(ll["lat"], 2), round(ll["lng"], 2)

def build_loc_lookup():
  existing_locs = json.loads(open("annualSingings_locs.json").read())
  loc_lookup = {}
  for title, loc, lat, lng, date, duration, book1, book2, book3, link in existing_locs:
    loc_lookup[loc] = [lat, lng]
  return loc_lookup

def start():
  annualSingings = json.loads(open("annualSingings.json").read())
  loc_lookup = build_loc_lookup()

  loc_annualSingings = []
  for row in annualSingings:
    try:
      title, loc, date, duration, book1, book2, book3, link = row
    except Exception:
      print(row)
      raise
    if loc in loc_lookup:
      lat, lng = loc_lookup[loc]
    else:
      lat, lng = lookup_ll(loc)
    loc_annualSingings.append([title, loc, lat, lng, date, duration, book1, book2, book3, link])
  with open("annualSingings_locs.json", "w") as outf:
    # monkey-patch json to round floats

    old_float_repr = encoder.FLOAT_REPR
    encoder.FLOAT_REPR = lambda o: format(o, '.2f')
    outf.write(json.dumps(loc_annualSingings).replace("],", "],\n"))
    encoder.FLOAT_REPR = old_float_repr

if __name__ == "__main__":
  start()
