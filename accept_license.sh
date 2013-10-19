expect -c 'set timeout -1; spawn android update sdk --filter 1,2,3,8,37,extra-android-support,extra-android-m2repository --no-ui --force ; expect {
  "Do you accept the license" { exp_send "y\r" ; exp_continue }
  eof }'
