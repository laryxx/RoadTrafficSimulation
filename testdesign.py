import json
import math
import re
import tqdm as tqdm
from kivy.config import Config
from kivy.uix.behaviors import ButtonBehavior
from kivy.uix.image import Image
Config.set('graphics', 'resizable', False)
import hashlib
import os
from kivy.clock import Clock
from kivy.properties import ListProperty, NumericProperty
import geojson
from kivy.app import App
from kivy.core.window import Window
from kivy.lang import Builder
from kivy.uix.widget import Widget
from kivy_garden.mapview import MapMarker, MapView
import sys
from kivy.graphics import Line
from kivy.uix.widget import Widget
Builder.load_file('testdesign1.kv')
import requests
from bs4 import BeautifulSoup

points_in_groups = []

my_layout_instance = None
my_mapview_instance = None


# Print methods
def print_concise_generation_rule_data(source_point):
    print(f"start lat: {source_point.start_road_node_lat}\n"
          f"start long: {source_point.start_road_node_long}\n"
          f"end lat: {source_point.end_road_node_lat}\n"
          f"end long: {source_point.end_road_node_long}\n"
          f"Intensity: {source_point.cars}\n"
          f"Trucks: {source_point.trucks}\n"
          f"Vans: {source_point.vans}\n"
          f"Sedans: {source_point.sedans}\n"
          f"hash: {source_point.hash_id}")


def set_coordinates_by_accessing_instance(lat, long):
    if my_layout_instance is not None:
        my_layout_instance.set_new_source_point_coordinates(lat, long)


# This method prints the bounding box coordinates of current mapview
def print_bound_box(mapview):
    # Get the bounding box of the map view
    # get_bbox returns the bounding box from the bottom-left to top-right
    bbox = mapview.get_bbox()
    lat1, long1, lat2, long2 = bbox
    # Print the bounding box coordinates
    print(f"Bottom left map border: \nLat: {lat1}, Long: {long1} \n Top-right map border: \nLat: {lat2}, Long: {long2}")


# TODO
# Convert the .osm map into geojson, search for the nearest road node, and return boolean value based on that
def is_distance_to_nearest_road_satisfactory(lat, long):
    return True


def is_source_point_data_satisfactory(vans, trucks, sedans, start_lat, start_long, end_lat, end_long, cars, name,
                                      start_road_lat, start_road_long, end_road_lat, end_road_long,
                                      start_group_id, end_group_id):
    # Check for at least one active checkbox
    if vans is False and trucks is False and sedans is False:
        print("At least one car type checkbox must be active")
        return False
    # Check if group ids are defined
    if start_group_id is None or end_group_id is None or start_group_id == 0 or end_group_id == 0:
        print("Error finding group id")
        return False
    # Check for correctness of coordinate values
    if (start_lat is None or start_long is None or start_lat == 0.0 or start_long == 0.0
            or end_lat is None or end_long is None or end_lat == 0.0 or end_long == 0.0
            or start_road_lat is None or start_road_long is None or start_road_lat == 0.0 or start_road_long == 0.0
            or end_road_lat is None or end_road_long is None or end_road_lat == 0.0 or end_road_long == 0.0):
        print(f"Invalid coordinates: {start_lat}, {start_long}, {end_lat}, {end_long}, {start_road_lat}, "
              f"{start_road_long}")
        return False
    # Check for appropriate source point name length
    if (12 > len(name) > 2) is False:
        print(f"Name {name} is too long or too short")
        return False
    # Check foe appropriate cars per epoch number
    if cars < 1 or cars > 5:
        print("Invalid number of cars per epoch")
        return False
    # Check if the road node is not already occupied
    if not is_road_node_free(start_road_lat, start_road_long) or not is_road_node_free(end_road_lat, end_road_long):
        print("Road point is already occupied. Please select a different area")
        return False
    # Check if the name is unique
    if name in my_layout_instance.generation_rules_names:
        print("Name is already taken")
        return False
    return True


def is_road_node_free(road_node_lat, road_node_long):
    pair = (road_node_lat, road_node_long)
    if pair not in my_layout_instance.taken_node_coordinate_pairs:
        return True
    else:
        return False


def are_map_bounds_correct(bbox):
    soup = BeautifulSoup(open('files\\schema.osm', encoding='utf8'), 'html.parser')
    try:
        minlat = float(soup.find('bounds')['minlat'])
        maxlat = float(soup.find('bounds')['maxlat'])
        minlon = float(soup.find('bounds')['minlon'])
        maxlon = float(soup.find('bounds')['maxlon'])
        lat1, long1, lat2, long2 = bbox
        print(
            f"Bottom left map border: \nLat: {lat1}, Long: {long1} \n Top-right map border: \nLat: {lat2}, Long: {long2}")
        print(
            f"Osm map min coordinates: \nMinLat: {minlat}, MinLong: {minlon} \n Osm map min coordinates: \nMaxLat: {maxlat}, MaxLong: {maxlon}")
    except TypeError:
        print("Error reading .osm file")


def is_float(value):
    try:
        float(value)
        return True
    except ValueError:
        return False


# NEW
def generate_output_json_file(generation_rules, daytime, duration, bbox):
    name = "Settings1"
    bottom_left_lat = bbox[0]
    bottom_left_long = bbox[1]
    top_right_lat = bbox[2]
    top_right_long = bbox[3]
    hash_id = create_composite_hash(name, bottom_left_lat, bottom_left_long, top_right_lat, top_right_long,
                                    generation_rules[0].name, generation_rules[0].start_lat,
                                    generation_rules[0].start_long, generation_rules[0].cars,
                                    generation_rules[0].start_road_node_lat, generation_rules[0].start_road_node_long)
    rules_dict = [rule.to_dict() for rule in generation_rules]
    data = {
        "data": {
            "identifiers": {
                "name": name,
                "hash": hash_id
            },
            "simulation_settings": {
                "daytime": daytime,
                "bottom_left_lat": bottom_left_lat,
                "bottom_left_long": bottom_left_long,
                "top_right_lat": top_right_lat,
                "top_right_long": top_right_long,
                "duration": duration
            },
            "generation_rules": rules_dict
        }
    }
    with open("files\\config.json", "w") as file:
        json.dump(data, file, indent=4)
    sys.exit()


class NodeGroup:
    group_id = ""
    number_of_nodes = 0
    nodes = []

    def __init__(self, group_id, number_of_nodes, nodes):
        self.group_id = group_id
        self.number_of_nodes = number_of_nodes
        if len(nodes) > 0:
            self.nodes = nodes


class RoadNode:
    lat = 0.0
    long = 0.0
    group_id = ""

    def __init__(self, lat, long, group_id):
        self.lat = lat
        self.long = long
        self.group_id = group_id


class GenerationRule:
    vans = False
    trucks = False
    sedans = False
    start_lat = 0.0
    start_long = 0.0
    end_lat = 0.0
    end_long = 0.0
    cars = 0
    name = ""
    start_road_node_lat = 0.0
    start_road_node_long = 0.0
    end_road_node_lat = 0.0
    end_road_node_long = 0.0
    hash_id = ""
    start_group_id = 0
    end_group_id = 0

    def __init__(self, vans, trucks, sedans, start_lat, start_long, end_lat, end_long, cars, name, start_road_node_lat,
                 start_road_node_long, end_road_node_lat, end_road_node_long, start_group_id, end_group_id):
        self.vans = vans
        self.trucks = trucks
        self.sedans = sedans
        self.start_lat = start_lat
        self.start_long = start_long
        self.end_lat = end_lat
        self.end_long = end_long
        self.cars = cars
        self.name = name
        self.start_road_node_lat = start_road_node_lat
        self.start_road_node_long = start_road_node_long
        self.end_road_node_lat = end_road_node_lat
        self.end_road_node_long = end_road_node_long
        self.hash_id = create_composite_hash(self.start_lat, self.start_long, self.start_road_node_lat,
                                             self.start_road_node_long, self.name)
        self.start_group_id = start_group_id
        self.end_group_id = end_group_id

    def print_data(self):
        print(f"\n ------ {self.name} source point data ------ \n\n")
        print(f"Vans: {self.vans} \n"
              f"Trucks: {self.trucks} \n"
              f"Sedans: {self.sedans} \n"
              f"Click Start Latitude: {self.start_lat} \n"
              f"Click Start Longitude: {self.start_long} \n"
              f"Click End Latitude: {self.end_lat} \n"
              f"Click End Longitude: {self.end_long} \n"
              f"Assigned start road node latitude: {self.start_road_node_lat} \n"
              f"Assigned start road node longitude: {self.start_road_node_long} \n"
              f"Assigned end road node latitude: {self.end_road_node_lat} \n"
              f"Assigned end road node longitude: {self.end_road_node_long} \n"
              f"Cars per epoch: {self.cars} \n"
              f"Start group id: {self.start_group_id} \n"
              f"End group id: {self.end_group_id} \n")

    def to_dict(self):
        return {
            "name": self.name,
            "start_lat": self.start_road_node_lat,
            "start_long": self.start_road_node_long,
            "end_lat": self.end_road_node_lat,
            "end_long": self.end_road_node_long,
            "vans": self.vans,
            "trucks": self.trucks,
            "sedans": self.sedans,
            "intensity": self.cars,
            "hash_id": self.hash_id,
            "start_group_id": self.start_group_id,
            "end_group_id": self.end_group_id
        }


def create_composite_hash(*args):
    combined_data = "".join(str(arg) for arg in args)
    sha256_hash = hashlib.sha256()
    sha256_hash.update(combined_data.encode('utf-8'))
    composite_hash = sha256_hash.hexdigest()
    return composite_hash


class StartMarker(ButtonBehavior, Image):
    lat = NumericProperty()
    lon = NumericProperty()
    _layer = None

    def __init__(self, lat, lon, **kwargs):
        super(StartMarker, self).__init__(**kwargs)
        self.source = "images\\start.png"
        self.text = "Start"
        self.size_hint = (None, None)
        self.width = 500
        self.height = 50
        self.anchor_x = 0.5
        self.anchor_y = 0.14
        self.lat = lat
        self.lon = lon

        # Set the initial lat and lon values
        self.lat = lat
        self.lon = lon

    def detach(self):
        if self._layer:
            self._layer.remove_widget(self)
            self._layer = None


class EndMarker(ButtonBehavior, Image):
    lat = NumericProperty()
    lon = NumericProperty()
    _layer = None

    def __init__(self, lat, lon, **kwargs):
        super(EndMarker, self).__init__(**kwargs)
        self.source = "images\\end.png"
        self.text = "End"
        self.size_hint = (None, None)
        self.width = 500
        self.height = 50
        self.anchor_x = 0.45
        self.anchor_y = 0.1
        self.lat = lat
        self.lon = lon

        # Set the initial lat and lon values
        self.lat = lat
        self.lon = lon

    def detach(self):
        if self._layer:
            self._layer.remove_widget(self)
            self._layer = None


class MyLayout(Widget):

    def __init__(self, **kwargs):
        super(MyLayout, self).__init__(**kwargs)
        global my_layout_instance
        my_layout_instance = self
        self.ids.new_source_point_lat.text = "Lat: " + "0.0"
        self.ids.new_source_point_long.text = "Long: " + "0.0"
        self.ids.map_new_center_on_coordinates_latitude.text = str(51.98612464170503)
        self.ids.map_new_center_on_coordinates_longitude.text = str(17.776237927246086)
        Clock.schedule_once(self.simulate_button_press, 5)

    def simulate_button_press(self, dt):
        self.restart_ui()
        print("====UI Loaded====")

    def center_on(self, lat, long):
        mapview = self.ids.mapview
        mapview.center_on(lat, long)
        self.osm_web_request()

    # Cat that looks like a bug(Important)
    print("           /\\_/\\  ")
    print("          ( o   o ) ")
    print("         (   =^=   )")
    print("          (        )")
    print("           (      )")
    print("           /\"---\"\\ ")

    taken_node_coordinate_pairs = []

    generation_rules = []

    markers = []

    # Checkbox values
    vans_clicked = False
    trucks_clicked = False
    sedans_clicked = False

    # Source point names collection
    #source_point_names = []

    generation_rules_names = []

    # Current new source point coordinates
    current_new_source_point_lat = 0.0
    current_new_source_point_long = 0.0

    # Spinner options for selecting point for demonstration
    spinner_options = ListProperty([])

    # All node groups on current map area
    node_groups = []

    def print_all_data_regarding_generation_rules(self):
        print("+--Current data defined--+")
        print("--generation rules names: ")
        for index, name in enumerate(self.generation_rules):
            print(f"{index}) {name}")
        print("----------------------\n")

    def find_nearest_road_node(self, lat, long):
        distance_list = []
        for group in self.node_groups:
            for coordinate_pair in group.nodes:
                # Calculating distance using Haversine formula:
                road_lat = coordinate_pair[0]
                road_long = coordinate_pair[1]
                lat1_rad = math.radians(lat)
                long1_rad = math.radians(long)
                lat2_rad = math.radians(road_lat)
                long2_rad = math.radians(road_long)
                radius = 6371
                # Calculating the differences between the latitudes and longitudes
                diff_lat = lat2_rad - lat1_rad
                diff_long = long2_rad - long1_rad
                a = math.sin(diff_lat / 2) ** 2 + math.cos(lat1_rad) * math.cos(lat2_rad) * math.sin(diff_long / 2) ** 2
                c = 2 * math.atan2(math.sqrt(a), math.sqrt(1 - a))
                distance = radius * c
                distance_list.append((distance, road_lat, road_long, group.group_id))
        minimum_distance = min(distance_list, key=lambda x: x[0])
        print(f"Minimum distance: {minimum_distance}km")

        print(f"Found road node: {minimum_distance[1]}, {float(minimum_distance[2])}, group id: {minimum_distance[3]}")

        return float(minimum_distance[1]), float(minimum_distance[2]), minimum_distance[3]

    def convert_map_to_geojson(self):
        geojson_file_exists = os.path.exists('files\\schema.geojson')
        osm_file_exists = os.path.exists('files\\schema.osm')
        if osm_file_exists and geojson_file_exists is False:
            print("geojson map file not found - creating it using osmtogeojson "
                  "(https://tyrasd.github.io/osmtogeojson/ (MIT License))...")
            os.system("osmtogeojson files/schema.osm > files/schema.geojson")
            # time.sleep(30)
            self.read_geojson()

        elif osm_file_exists and geojson_file_exists:
            print("Map area has changed - creating new geojson file")
            os.remove('files\\schema.geojson')
            print("Creating geojson file using osmtogeojson "
                  "(https://tyrasd.github.io/osmtogeojson/ (MIT License))...")
            os.system("osmtogeojson files/schema.osm > files/schema.geojson")
            # time.sleep(30)
            self.read_geojson()
        else:
            print("Error - osm file does not exist")

    def read_geojson(self):
        coordinates_latitude = []
        coordinates_longitude = []
        groups_and_points = []

        with open("files\\schema.geojson", encoding="utf8") as f:
            gj = geojson.load(f)

            for feature in gj['features']:
                if 'highway' in feature['properties']:
                    highway = feature['properties']['highway']
                    if highway == "residential" or highway == "motorway" or highway == "trunk" or \
                            highway == "primary" or highway == "secondary" or highway == "tertiary" or \
                            highway == "unclassified" or highway == "service" or highway == "track" or \
                            highway == "road" or highway == "pedestrian" or highway == "motorway_link" or \
                            highway == "trunk_link" or highway == "primary_link" or highway == "secondary_link" or \
                            highway == "tertiary_link" or highway == "road" or highway == "living_street" or \
                            highway == "raceway" or highway == "disused":
                        way = feature['properties']['id']
                        group_id = re.sub('[^0-9]', '', way)
                        number_of_elements = 0
                        data = feature['geometry']['coordinates']
                        for coordinate_set in data:
                            a = 0
                            number_of_elements += 1
                            for coordinate in coordinate_set:
                                a = a + 1
                                if a == 1:
                                    coordinates_latitude.append(float(coordinate))
                                elif a == 2:
                                    coordinates_longitude.append(float(coordinate))
                        if len(coordinates_latitude) == len(coordinates_longitude):
                            group = self.create_group(group_id, coordinates_latitude, coordinates_longitude,
                                                      number_of_elements)
                            self.node_groups.append(group)
                        else:
                            print("Critical file error")
        print(f"Number of node groups: {len(self.node_groups)}")

    def create_group(self, group_id, coordinates_latitude, coordinates_longitude, number_of_elements):
        nodes = []
        for index, coordinate in enumerate(coordinates_latitude):
            # Must switch places
            nodes.append((coordinates_longitude[index], coordinates_latitude[index]))
        group = NodeGroup(group_id, number_of_elements, nodes)
        return group

    # TODO
    # This method refreshes the mapview doing a different center_on and
    # empties all that was previously defined by the user, including the marker.
    def restart_ui(self):
        self.node_groups = []
        self.spinner_options = []
        self.taken_node_coordinate_pairs = []
        self.generation_rules = []
        self.generation_rules_names = []

        self.current_new_source_point_lat = 0.0
        self.current_new_source_point_long = 0.0
        lat = str(self.ids.map_new_center_on_coordinates_latitude.text)
        long = str(self.ids.map_new_center_on_coordinates_longitude.text)
        if is_float(lat) and is_float(long):
            lat = float(lat)
            long = float(long)
            self.center_on(lat, long)
        else:
            print("Please enter a numeric floating point (e.x. 12.345) value")

    # NEW
    def show_selected_generation_rule_on_map(self):
        print("show_selected_generation_rule_on_map Called")
        value = self.ids.select_defined_point_spinner.text
        rule_name = value.split(":")[0].strip()
        for rule in self.generation_rules:
            if rule_name == rule.name:
                print(f"Rule found: {rule.start_road_node_lat}, {rule.start_road_node_long}, \n, "
                      f"{rule.end_road_node_lat}, {rule.end_road_node_long}")
                lat1 = float(rule.start_road_node_lat)
                long1 = float(rule.start_road_node_long)
                lat2 = float(rule.end_road_node_lat)
                long2 = float(rule.end_road_node_long)
                start_marker = StartMarker(lat=lat1, lon=long1)
                end_marker = EndMarker(lat=lat2, lon=long2)
                my_mapview_instance.change_markers(start_marker, end_marker)


    def fill_coordinates(self):
        mapview = self.ids.mapview
        lat, long = str(self.current_new_source_point_lat), str(self.current_new_source_point_long)
        self.ids.map_new_center_on_coordinates_latitude.text = lat
        self.ids.map_new_center_on_coordinates_longitude.text = long


    def update_spinner_point_list(self):
        self.spinner_options.clear()
        for rule in self.generation_rules:
            lat = str(rule.start_road_node_lat)[:8]
            long = str(rule.start_road_node_long)[:8]
            value = f"{str(rule.name)}: {lat}, {long}"
            self.spinner_options.append(value)

    # This method updates the current new source point coordinates from marker coordinates
    def set_new_source_point_coordinates(self, lat, long):
        self.ids.new_source_point_lat.text = "Lat: " + str(lat)[:8]
        self.ids.new_source_point_long.text = "Long: " + str(long)[:8]
        self.current_new_source_point_lat = float(lat)
        self.current_new_source_point_long = float(long)

    # Checkboxes menu
    def click_vans(self, instance, value):
        if self.vans_clicked is False:
            self.vans_clicked = True
        else:
            self.vans_clicked = False

    def click_trucks(self, instance, value):
        if self.trucks_clicked is False:
            self.trucks_clicked = True
        else:
            self.trucks_clicked = False

    def click_sedans(self, instance, value):
        if self.sedans_clicked is False:
            self.sedans_clicked = True
        else:
            self.sedans_clicked = False

    # Simulation duration slider/label update method
    def slider_simulation_settings_duration_triggered(self, *args):
        if str(int(args[1])) == "1":
            self.slider_simulation_duration_text.text = "1 minute"
        else:
            self.slider_simulation_duration_text.text = str(int(args[1])) + " minutes"

    # Cars per epoch as a new source point setting slider/label update method
    def slider_new_source_point_cars_per_epoch_triggered(self, *args):
        self.slider_cars_per_epoch_text.text = "Cars per epoch: " + str(int(args[1]))


    def remove_point_button_clicked(self):
        text = self.ids.select_defined_point_spinner.text
        print("Removing: " + text)
        name = text.split(":")[0]
        for rule in self.generation_rules:
            if rule.name == name:
                self.generation_rules.remove(rule)
        self.update_spinner_point_list()


    def add_point_button_clicked(self):
        self.print_all_data_regarding_generation_rules()
        checkbox_vans = self.vans_clicked
        checkbox_trucks = self.trucks_clicked
        checkbox_sedans = self.sedans_clicked
        start_lat = float(my_mapview_instance.markers[0].lat)
        start_long = float(my_mapview_instance.markers[0].lon)
        end_lat = float(my_mapview_instance.markers[1].lat)
        end_long = float(my_mapview_instance.markers[1].lon)
        start_road_lat, start_road_long, start_group_id = self.find_nearest_road_node(start_lat, start_long)
        end_road_lat, end_road_long, end_group_id = self.find_nearest_road_node(end_lat, end_long)
        name = str(self.ids.new_source_point_name_text_input.text)
        cars = int(str(self.slider_cars_per_epoch_text.text)[16:])
        print(f"start_lat: {start_lat}, start_long: {start_long}, end_lat: {end_lat}, end_long: {end_long}")
        if is_source_point_data_satisfactory(checkbox_vans, checkbox_trucks, checkbox_sedans, start_lat, start_long,
                                             end_lat, end_long, cars, name, start_road_lat, start_road_long,
                                             end_road_lat, end_road_long, start_group_id, end_group_id):
            self.generation_rules_names.append(name)
            generation_rule = GenerationRule(checkbox_vans, checkbox_trucks, checkbox_sedans, start_lat, start_long,
                                             end_lat, end_long, cars, name, start_road_lat, start_road_long,
                                             end_road_lat, end_road_long, start_group_id, end_group_id)
            self.taken_node_coordinate_pairs.append((generation_rule.start_road_node_lat,
                                                     generation_rule.start_road_node_long))
            self.taken_node_coordinate_pairs.append((generation_rule.end_road_node_lat,
                                                     generation_rule.end_road_node_long))
            self.generation_rules.append(generation_rule)
            self.update_spinner_point_list()
        else:
            print("Defined data is unsatisfactory")
            pass


    class CustomMapView(ButtonBehavior, MapView):
        def __init__(self, **kwargs):
            super(MyLayout.CustomMapView, self).__init__()
            global my_mapview_instance
            my_mapview_instance = self
            image_path = "images\\end3.png"
            self.markers = []


        def get_bounding_box(self):
            mapview = MapView
            return mapview.get_bbox(self)


        def remove_oldest_marker(self):
            if len(self.markers) > 2:
                oldest_marker = self.markers.pop(0)
                mapview = MapView
                self.remove_widget(oldest_marker)
                # self.remove_widget(oldest_marker)
                mapview.remove_widget(self, oldest_marker)
                mapview.remove_marker(self, oldest_marker)

        def remove_all_markers(self):
            mapview = MapView
            for marker in self.markers:
                self.remove_widget(marker)
                mapview.remove_widget(self, marker)
                mapview.remove_marker(self, marker)
            self.markers.clear()

        def add_a_marker(self, marker):
            pass

        def change_markers(self, start_marker, end_marker):
            mapview = MapView
            self.remove_all_markers()
            self.markers.append(start_marker)
            mapview.add_marker(self, start_marker)
            self.markers.append(end_marker)
            mapview.add_marker(self, end_marker)

        def replace_marker(self, end_marker):
            # remove oldest, change the 2nd to Start, add new End
            marker_0 = self.markers[0]
            marker_1 = self.markers[1]
            start_marker = StartMarker(lat=marker_1.lat, lon=marker_1.lon)
            self.remove_all_markers()
            self.markers.append(start_marker)
            self.markers.append(end_marker)
            mapview = MapView
            mapview.add_marker(self, start_marker)
            mapview.add_marker(self, end_marker)

        def on_touch_down(self, touch):
            mapview = MapView

            # Checking if the touch event is within the bounds of MapView
            if not mapview.collide_point(self, *touch.pos):
                return False

            # Converting the touch position to map coordinates while accounting for "x": 0.25 offset
            lat, lon = mapview.get_latlon_at(self, touch.pos[0] - self.parent.width * 0.25, touch.pos[1])

            # To ignore touches outside the map area
            if lat is None or lon is None:
                return False


            if len(self.markers) < 1:
                marker = StartMarker(lat=float(lat), lon=float(lon))
                self.markers.append(marker)
                mapview.add_marker(self, marker)
            elif 0 < len(self.markers) < 2:
                marker = EndMarker(lat=float(lat), lon=float(lon))
                self.markers.append(marker)
                mapview.add_marker(self, marker)
            elif len(self.markers) > 1:
                marker = EndMarker(lat=float(lat), lon=float(lon))
                self.replace_marker(marker)

            print(f"Number of markers: {len(self.markers)}")

            # Printing marker coordinates
            print(f"Lat: {str(lat)}, Long: {str(lon)}")

            parent_obj = self.parent
            par = mapview.parent
            print(parent_obj)

            set_coordinates_by_accessing_instance(lat, lon)

            return True

    # This method shall collect all the data that was defined, write it to a JSON file and end execution
    def start_button_clicked(self):
        daytime = self.ids.simulation_settings_daytime_spinner.text
        if daytime is "Day" or daytime is "Morning" or daytime is "Night":
            print(f"daytime: {daytime}")
            duration = self.ids.simulation_settings_duration_slider.value
            print(f"duration: {duration}")
            bbox = my_mapview_instance.get_bounding_box()
            print(f"bbox: {bbox}")
            generate_output_json_file(self.generation_rules, daytime, duration, bbox)
        else:
            print("Daytime not selected")

    def osm_web_request(self):
        mapview = self.ids.mapview
        print("Downloading map...")
        bbox = mapview.get_bbox()
        print("Bounding box: ", bbox)
        query = f"""
            [bbox:{bbox[0]},{bbox[1]},{bbox[2]},{bbox[3]}];
            (
                way[highway=primary]({bbox[0]},{bbox[1]},{bbox[2]},{bbox[3]});
                way[highway=secondary]({bbox[0]},{bbox[1]},{bbox[2]},{bbox[3]});
                way[highway=tertiary]({bbox[0]},{bbox[1]},{bbox[2]},{bbox[3]});
                way[highway=residential]({bbox[0]},{bbox[1]},{bbox[2]},{bbox[3]});
                way[highway=unclassified]({bbox[0]},{bbox[1]},{bbox[2]},{bbox[3]});
                way[highway=service]({bbox[0]},{bbox[1]},{bbox[2]},{bbox[3]});
                way[highway=motorway]({bbox[0]},{bbox[1]},{bbox[2]},{bbox[3]});
                way[highway=trunk]({bbox[0]},{bbox[1]},{bbox[2]},{bbox[3]});
                way[highway=pedestrian]({bbox[0]},{bbox[1]},{bbox[2]},{bbox[3]});
                way[highway=motorway_link]({bbox[0]},{bbox[1]},{bbox[2]},{bbox[3]});
                way[highway=trunk_link]({bbox[0]},{bbox[1]},{bbox[2]},{bbox[3]});
                way[highway=primary_link]({bbox[0]},{bbox[1]},{bbox[2]},{bbox[3]});
                way[highway=secondary_link]({bbox[0]},{bbox[1]},{bbox[2]},{bbox[3]});
                way[highway=tertiary_link]({bbox[0]},{bbox[1]},{bbox[2]},{bbox[3]});
                way[highway=road]({bbox[0]},{bbox[1]},{bbox[2]},{bbox[3]});
                way[highway=living_street]({bbox[0]},{bbox[1]},{bbox[2]},{bbox[3]});
                way[highway=raceway]({bbox[0]},{bbox[1]},{bbox[2]},{bbox[3]});
                way[highway=disused]({bbox[0]},{bbox[1]},{bbox[2]},{bbox[3]});
                node({bbox[0]},{bbox[1]},{bbox[2]},{bbox[3]});
            );
            out meta;
        """

        # Define the Overpass API endpoint
        overpass_url = "https://overpass-api.de/api/interpreter"

        # Define the request payload
        payload = {
            "data": query
        }

        # Send the POST request to the Overpass API
        response = requests.post(overpass_url, data=payload, stream=True)
        total_size_in_bytes = int(response.headers.get('content-length', 0))
        block_size = 1024  # 1 Kibobyte
        progress_bar = tqdm.tqdm(total=total_size_in_bytes, unit='iB', unit_scale=True)
        # Check if the request was successful
        if response.status_code == 200:
            # Write the response content to a file
            with open('files\\schema.osm', 'wb') as f:
                for data in response.iter_content(block_size):
                    progress_bar.update(len(data))
                    f.write(data)

            are_map_bounds_correct(bbox)
            self.convert_map_to_geojson()
        else:
            # Display an error message
            print("Error:", response.status_code)


class AwesomeApp(App):
    def build(self):
        Window.size = (880, 660)
        Window.clearcolor = (0.7, 0.7, 0.7, 1)
        return MyLayout()


if __name__ == "__main__":
    AwesomeApp().run()
