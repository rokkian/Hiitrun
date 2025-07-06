# Hiitrun

WearOs app for running exercises.
I was not satisfied with the running features of the Fitbit app and even the premium version.
So i decided to give it a try creating the kind of app for running sessions I envision to my needs.

For suggestions feel free to open issues :)

## Ideas:

  - A companion mobile app for the WearOs watch app might be necessary for connecting to external APIs
  - Also for implementing the user authentication
  => Still I will try to implement my WearOs app as standalone as possible, until functionally possible

>> Main moat: I want to be assisted by an app on the Pixelwatch that can guide me in HIIT run sessions. I want the sessions to contain multple phases of different activity. The training must be variagate and stimulating.

>> Human machine interface: the app can communicate with the user via:
- Vibrations on the watch (perhaps setting the cadence)
- Voice guiding the training (generated voice of the PT guiding the session)

## Implementation tools/tech
In order to implemnt such vision I am considering the use of: Google AI studio, voice generator, Gemini, possibly some custom model.
The sessions should follow a defined data format that the PixelWatch app can read and execute.
Given the UI premises the format will be something like a json plus an mp3/aac audio file.

Example of a 30 minute training:
```json
{
  "session_name": "30-Minute Weight Loss Running Workout",
  "total_duration": "30 minutes",
  "target_audience": "Average fitness level, weight loss goal",
  "phases": [
    {
      "phase": "Warm-up",
      "duration": "10 minutes",
      "activities": [
        {
          "activity": "Easy walk",
          "duration": "3 minutes",
          "intensity": "Very light",
          "steps_per_minute": 90,
          "description": "Start with a comfortable walking pace to activate muscles"
        },
        {
          "activity": "Brisk walk",
          "duration": "3 minutes",
          "intensity": "Light",
          "steps_per_minute": 100,
          "description": "Increase pace to a brisk walk, swing arms naturally"
        },
        {
          "activity": "Light jog",
          "duration": "4 minutes",
          "intensity": "Moderate",
          "steps_per_minute": 140,
          "description": "Begin jogging at conversational pace - you should be able to talk"
        }
      ]
    },
    {
      "phase": "Main Workout - Interval Training",
      "duration": "16 minutes",
      "activities": [
        {
          "activity": "Moderate run",
          "duration": "2 minutes",
          "intensity": "Moderate",
          "steps_per_minute": 140,
          "description": "Steady pace run, slightly faster than warm-up jog"
        },
        {
          "activity": "Fast run",
          "duration": "1 minute",
          "intensity": "High",
          "steps_per_minute": 160,
          "description": "Increase pace significantly but not all-out sprint"
        },
        {
          "activity": "Recovery jog",
          "duration": "1 minute",
          "intensity": "Light-moderate",
          "steps_per_minute": 120,
          "description": "Slow down to easy jog to recover"
        },
        {
          "activity": "Moderate run",
          "duration": "2 minutes",
          "intensity": "Moderate",
          "steps_per_minute": 140,
          "description": "Return to steady moderate pace"
        },
        {
          "activity": "Sprint",
          "duration": "30 seconds",
          "intensity": "Very high",
          "steps_per_minute": 180,
          "description": "Short burst at 80-85% maximum effort"
        },
        {
          "activity": "Recovery walk/jog",
          "duration": "1.5 minutes",
          "intensity": "Light",
          "steps_per_minute": 100,
          "description": "Walk or very light jog to recover from sprint"
        },
        {
          "activity": "Moderate run",
          "duration": "2 minutes",
          "intensity": "Moderate",
          "steps_per_minute": 140,
          "description": "Steady pace run"
        },
        {
          "activity": "Fast run",
          "duration": "1 minute",
          "intensity": "High",
          "steps_per_minute": 160,
          "description": "Increase pace significantly"
        },
        {
          "activity": "Recovery jog",
          "duration": "1 minute",
          "intensity": "Light-moderate",
          "steps_per_minute": 120,
          "description": "Slow down to easy jog"
        },
        {
          "activity": "Sprint",
          "duration": "30 seconds",
          "intensity": "Very high",
          "steps_per_minute": 180,
          "description": "Final sprint at 80-85% maximum effort"
        },
        {
          "activity": "Recovery walk/jog",
          "duration": "3.5 minutes",
          "intensity": "Light",
          "steps_per_minute": 100,
          "description": "Gradual cool-down transition"
        }
      ]
    },
    {
      "phase": "Cool-down",
      "duration": "4 minutes",
      "activities": [
        {
          "activity": "Easy walk",
          "duration": "4 minutes",
          "intensity": "Very light",
          "steps_per_minute": 90,
          "description": "Slow walk to gradually lower heart rate and prevent dizziness"
        }
      ]
    }
  ],
  "key_benefits": [
    "Burns calories efficiently through interval training",
    "Improves cardiovascular fitness",
    "Boosts metabolism for hours after workout",
    "Builds endurance gradually",
    "Prevents overexertion with built-in recovery periods"
  ],
  "safety_notes": [
    "Listen to your body - reduce intensity if feeling overly fatigued",
    "Stay hydrated before, during, and after the session",
    "If you experience chest pain, dizziness, or severe shortness of breath, stop immediately",
    "Adjust sprint intensity based on your current fitness level",
    "Consider this workout 2-3 times per week with rest days between"
  ],
  "intensity_scale": {
    "Very light": "50-60% max heart rate",
    "Light": "60-70% max heart rate",
    "Light-moderate": "65-75% max heart rate",
    "Moderate": "70-80% max heart rate",
    "High": "80-90% max heart rate",
    "Very high": "85-95% max heart rate"
  },
  "steps_per_minute_guide": {
    "90 steps/min": "Comfortable walking pace",
    "100 steps/min": "Brisk walking or light recovery",
    "120 steps/min": "Easy jogging pace",
    "140 steps/min": "Moderate running pace",
    "160 steps/min": "Fast running pace",
    "180 steps/min": "Sprint pace"
  }
}
```

## Features

  - Position related: by using location GPS will try to detect the speed of the user
  - Main feature: training sessions
    - The user will set/prompt the app for the correct run training session:
      -Set: setting can be done by adjusting parameters on the app
      -Prompting generation: The user will be able to describe the kind ofrunning workout he wishes and the app will query an LLM for the generation of such a training session.


  
