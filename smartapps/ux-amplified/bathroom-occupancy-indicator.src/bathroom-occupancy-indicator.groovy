/**
 *  Bathroom Occupancy Indicator
 *
 *  Copyright 2017 Jack Beck
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at:
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 *  for the specific language governing permissions and limitations under the License.
 *
 */
definition(
    name: "Bathroom Occupancy Indicator",
    namespace: "ux.amplified",
    author: "Jack Beck",
    description: "Changes the color of an light to indicate bathroom occupancy.\r\n",
    category: "My Apps",
    iconUrl: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience.png",
    iconX2Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png",
    iconX3Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png")


preferences {
	section("Bathroom Sensor") {
		input "BRS1", "capability.motionSensor", required: true, title: "First Bathroom Sensor:"
        input "BRS2", "capability.motionSensor", required: true, title: "Second Bathroom Sensor:"
	}
    
    section("Indicator Light") {
    	input "L1", "capability.colorControl", required: true, title: "Indicator Light:", multiple: false
    }
}

def installed() {
	log.debug "Installed with settings: ${settings}"
    
	initialize()
}

def updated() {
	log.debug "Updated with settings: ${settings}"

	unsubscribe()
	initialize()
}

def initialize() {
	subscribe(BRS1, "motion", setIndicator)
    subscribe(BRS2, "motion", setIndicator)
}

def setIndicator(evt) {
	def RED = [hue: 100, saturation: 100]
    def YELLOW = [hue: 15, saturation: 100]
    def GREEN = [hue: 32, saturation: 100]
    def colors = [GREEN, YELLOW, RED]
    
    def occupancy = 0
    if(BRS1.currentMotion=='active') occupancy ++
    if(BRS2.currentMotion=='active') occupancy ++
    
    log.debug "Setting color to ${colors[occupancy]}."
    L1.setColor(colors[occupancy])
    
    log.debug "Occupancy ${occupancy}"
	log.debug "BRS1 ${BRS1.currentMotion}"
    log.debug "BRS2 ${BRS2.currentMotion}"
}