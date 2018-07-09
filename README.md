# find-sacred-harp
Search for nearby sacred harp events by zipcode.


1) minutesReader.java parses minutes.txt and generates annualSingings.json.
2) lookup_locs.py gets a latitude and longitude for each event and generates annualSingings_locs.json
3) index.html gets the zipcode and finds its latitude and longitude from zipcode.json
4) index.html gets the distance from the zipcode location to the event locations in annualSingings_locs.json, as well as the time until each event
5) index.html generates a list of singing events within the given radius and timeframe

Many thanks to Jeff Kaufman for sharing his code for trycontra.com, which formed a foundation for this project.
