import sys
sys.stdout.reconfigure(encoding='utf-8')
from faster_whisper import WhisperModel

audio_path = sys.argv[1]
model = WhisperModel("tiny", device="cpu", compute_type="int8")
segments, _ = model.transcribe(audio_path, language="pt")

for segment in segments:
    print(segment.text, end="", flush=True)