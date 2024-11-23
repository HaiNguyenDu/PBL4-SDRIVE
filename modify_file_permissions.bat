@echo off
icacls "\\192.168.1.25\SDriver\XPhuc\haha.docx" /inheritance:r
icacls "\\192.168.1.25\SDriver\XPhuc\haha.docx" /remove "PBL4\Thanhan"
icacls "\\192.168.1.25\SDriver\XPhuc\haha.docx" /grant "PBL4\Thanhan":R
icacls "\\192.168.1.25\SDriver\XPhuc\haha.docx" /grant "PBL4\Administrator:F"
icacls "\\192.168.1.25\SDriver\XPhuc\haha.docx" /grant "PBL4\XPhuc:F"
